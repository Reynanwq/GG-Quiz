package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.domain.repositories.RankingSnapshotRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

/**
 * Versão REATIVA do FindRankingUseCase.
 *
 * O use case original (FindRankingUseCase) faz UMA consulta bloqueante e retorna Page<RankingSnapshot>.
 *
 * Este use case reativo demonstra como buscar os 4 períodos (DAILY, WEEKLY, MONTHLY, ALLTIME)
 * em PARALELO, ao invés de sequencialmente, usando o Project Reactor.
 *
 * FLUXO:
 *  Flux.fromIterable(4 períodos)
 *    └── flatMap → consulta cada período em paralelo (Schedulers.boundedElastic)
 *          └── map → transforma RankingSnapshot em RankingPeriodResult
 *                └── collectList → agrupa tudo em Mono<List<RankingPeriodResult>>
 */
@RequiredArgsConstructor
public class FindRankingReactiveUseCase {

    private final RankingSnapshotRepository rankingSnapshotRepository;

    // Quantos itens retornar por período no endpoint reativo
    private static final int PAGE_SIZE = 10;

    /**
     * Busca o ranking de TODOS os períodos em paralelo para uma região.
     *
     * @param regionId null = ranking global
     * @return Mono com a lista de resultados por período
     */
    public Mono<List<RankingPeriodResult>> executeAllPeriods(Integer regionId) {

        /*
         * PASSO 1 — Flux.fromIterable
         *
         * Transforma o array de enums RankingPeriod em um Flux.
         * Cada item emitido será: DAILY, WEEKLY, MONTHLY, ALLTIME
         *
         * Isso nos dá um "fluxo" de períodos para processar.
         */
        return Flux.fromIterable(List.of(RankingPeriod.values()))

                /*
                 * PASSO 2 — flatMap
                 *
                 * Para CADA período emitido, disparamos uma consulta ao banco.
                 * O flatMap, diferente do map, aceita que a função interna retorne
                 * um Publisher (Mono/Flux) e achata o resultado em um único fluxo.
                 *
                 * IMPORTANTE: o flatMap dispara todas as consultas SIMULTANEAMENTE
                 * (não espera a DAILY terminar para começar a WEEKLY).
                 * Isso é o ganho real de performance vs. o código bloqueante original.
                 *
                 * Se quiséssemos manter a ORDEM dos períodos, usaríamos flatMapSequential.
                 */
                .flatMap(period -> fetchPeriod(regionId, period))

                /*
                 * PASSO 3 — collectList
                 *
                 * Converte o Flux<RankingPeriodResult> (stream de itens) em um
                 * Mono<List<RankingPeriodResult>> (um único valor com todos os resultados).
                 *
                 * Só emite quando TODOS os flatMaps acima completarem.
                 */
                .collectList();
    }

    /**
     * Busca o ranking de UM período específico para uma região.
     *
     * @param regionId null = global
     * @param period   período desejado
     * @return Mono com o resultado do período
     */
    public Mono<RankingPeriodResult> executeOnePeriod(Integer regionId, String period) {
        RankingPeriod rankingPeriod = RankingPeriod.valueOf(period.toUpperCase());
        return fetchPeriod(regionId, rankingPeriod);
    }

    // =========================================================
    // MÉTODO INTERNO: busca um período e encapsula o resultado
    // =========================================================

    private Mono<RankingPeriodResult> fetchPeriod(Integer regionId, RankingPeriod period) {

        LocalDate periodStart = resolvePeriodStart(period);

        /*
         * Mono.fromCallable
         *
         * O RankingSnapshotRepository ainda usa JPA (bloqueante).
         * O fromCallable envolve essa chamada bloqueante em um Mono,
         * mas de forma LAZY: a consulta só é executada quando alguém
         * fizer .subscribe() — nunca antes.
         *
         * Sem fromCallable, a consulta seria executada imediatamente
         * ao montar o pipeline, bloqueando a thread principal.
         */
        return Mono.fromCallable(() ->
                        rankingSnapshotRepository
                                .findRanking(regionId, period, periodStart,
                                        org.springframework.data.domain.PageRequest.of(0, PAGE_SIZE))
                                .getContent()
                )
                /*
                 * subscribeOn(Schedulers.boundedElastic)
                 *
                 * Aqui está o ponto mais importante desta implementação.
                 *
                 * Como o repositório usa JPA (bloqueante), NÃO podemos executá-lo
                 * em uma thread reativa (event loop). O BlockHound detectaria isso
                 * e lançaria um erro.
                 *
                 * O boundedElastic é um pool de threads criado especificamente para
                 * operações bloqueantes de I/O (banco de dados, arquivos, chamadas HTTP legadas).
                 * Ele "isola" o bloqueio, liberando as threads reativas para continuar
                 * processando outras requisições enquanto o banco responde.
                 *
                 * subscribeOn define em qual thread o fromCallable acima será executado.
                 * Como é o único subscribeOn neste Mono, afeta toda a cadeia acima dele.
                 */
                .subscribeOn(Schedulers.boundedElastic())

                /*
                 * map
                 *
                 * Transforma a List<RankingSnapshot> retornada pelo banco em
                 * nosso DTO RankingPeriodResult. Operação síncrona e leve,
                 * não precisa de subscribeOn separado.
                 */
                .map(snapshots -> new RankingPeriodResult(period, snapshots));
    }

    // =========================================================
    // HELPER: resolve a data de início de cada período
    // (mesma lógica do FindRankingUseCase original)
    // =========================================================

    private LocalDate resolvePeriodStart(RankingPeriod period) {
        LocalDate today = LocalDate.now();
        return switch (period) {
            case DAILY   -> today;
            case WEEKLY  -> today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
            case MONTHLY -> today.with(TemporalAdjusters.firstDayOfMonth());
            case ALLTIME -> LocalDate.of(2000, 1, 1);
        };
    }

    // =========================================================
    // DTO interno: resultado de um período
    // =========================================================

    /**
     * Agrupa os snapshots de um período específico.
     * Record do Java 17+ — imutável por padrão, ideal para DTOs de pipeline reativo.
     */
    public record RankingPeriodResult(
            RankingPeriod period,
            List<RankingSnapshot> snapshots
    ) {}
}