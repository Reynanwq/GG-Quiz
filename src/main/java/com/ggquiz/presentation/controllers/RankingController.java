package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.FindMyRankingPositionUseCase;
import com.ggquiz.application.usecases.FindRankingReactiveUseCase;
import com.ggquiz.application.usecases.FindRankingUseCase;
import com.ggquiz.application.usecases.RateLimiterPort;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.presentation.dto.response.MyPositionResponse;
import com.ggquiz.presentation.dto.response.RankingReactiveResponse;
import com.ggquiz.presentation.dto.response.RankingResponse;
import com.ggquiz.presentation.mappers.PresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final FindRankingUseCase findRankingUseCase;
    private final FindRankingReactiveUseCase findRankingReactiveUseCase; // NOVO
    private final FindMyRankingPositionUseCase findMyRankingPositionUseCase;
    private final UserRepository userRepository;
    private final PresentationMapper mapper;
    private final RateLimiterPort rateLimiterPort;

    // =========================================================
    // ENDPOINTS ORIGINAIS — não foram tocados
    // =========================================================

    // GET /api/ranking?period=WEEKLY&regionId=1&page=0&size=10
    @GetMapping
    public Page<RankingResponse> getRanking(@RequestParam(required = false) Integer regionId,
                                            @RequestParam(defaultValue = "ALLTIME") String period,
                                            Pageable pageable, Authentication auth) {
        rateLimiterPort.checkLimit(auth.getName());
        return findRankingUseCase.execute(regionId, period, pageable).map(mapper::toRankingResponse);
    }

    // GET /api/ranking/me?period=ALLTIME&regionId=1
    @GetMapping("/me")
    public ResponseEntity<MyPositionResponse> getMyPosition(
            @RequestParam(required = false) Integer regionId,
            @RequestParam(defaultValue = "ALLTIME") String period,
            Authentication auth) {

        rateLimiterPort.checkLimit(auth.getName());

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        return findMyRankingPositionUseCase.execute(user, regionId, period)
                .map(r -> ResponseEntity.ok(new MyPositionResponse(
                        r.position(), r.username(), r.bestRating(), r.totalAttempts())))
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================================================
    // NOVO ENDPOINT REATIVO
    // GET /api/ranking/reactive?regionId=1
    //
    // Retorna os 4 períodos de uma vez, buscados em paralelo.
    // Demonstra o pipeline reativo completo com comentários didáticos.
    // =========================================================

    /**
     * Endpoint reativo que busca todos os períodos do ranking em paralelo.
     *
     * DIFERENÇA vs. endpoint original:
     *
     * Endpoint original:
     *   → 1 requisição = 1 consulta ao banco (bloqueante) = 1 período
     *   → Para ver os 4 períodos, o cliente faz 4 chamadas sequenciais
     *
     * Endpoint reativo:
     *   → 1 requisição = 4 consultas ao banco (em paralelo) = 4 períodos
     *   → O Spring aguarda o Mono<> completar antes de serializar a resposta
     *   → As threads reativas ficam livres enquanto o banco processa
     *
     * ATENÇÃO: O Spring MVC (que este projeto usa) consegue retornar Mono<>
     * diretamente desde que spring-webflux esteja no classpath como dependência.
     * O projeto não precisa migrar para WebFlux completamente para isso funcionar.
     * Basta adicionar ao pom.xml:
     *
     *   <dependency>
     *     <groupId>org.springframework.boot</groupId>
     *     <artifactId>spring-boot-starter-webflux</artifactId>
     *   </dependency>
     *
     * GET /api/ranking/reactive?regionId=1
     * GET /api/ranking/reactive          (sem regionId = global)
     */
    @GetMapping("/reactive")
    public Mono<RankingReactiveResponse> getRankingReactive(
            @RequestParam(required = false) Integer regionId,
            Authentication auth) {

        rateLimiterPort.checkLimit(auth.getName());

        /*
         * executeAllPeriods retorna Mono<List<RankingPeriodResult>>.
         *
         * O .map() aqui transforma esse resultado em nosso DTO de resposta.
         * É uma operação síncrona e leve (apenas mapeamento de objetos),
         * por isso não precisa de subscribeOn.
         *
         * O Spring MVC detecta que o retorno é um Mono<> e aguarda
         * sua conclusão antes de serializar o JSON — sem bloquear threads.
         */
        return findRankingReactiveUseCase.executeAllPeriods(regionId)
                .map(periods -> {

                    /*
                     * Converte cada RankingPeriodResult em um PeriodBlock do DTO.
                     *
                     * AtomicInteger para posição: usamos AtomicInteger ao invés de
                     * um int simples porque lambdas exigem variáveis efetivamente finais.
                     * O AtomicInteger é um container mutável — o valor dentro dele muda,
                     * mas a referência ao container permanece final.
                     */
                    List<RankingReactiveResponse.PeriodBlock> blocks = periods.stream()
                            .map(periodResult -> {
                                AtomicInteger position = new AtomicInteger(1);

                                List<RankingReactiveResponse.RankingEntry> entries =
                                        periodResult.snapshots().stream()
                                                .map(snapshot -> new RankingReactiveResponse.RankingEntry(
                                                        position.getAndIncrement(),
                                                        snapshot.getUser().getUsername(),
                                                        snapshot.getBestRating(),
                                                        snapshot.getTotalAttempts()
                                                ))
                                                .toList();

                                return new RankingReactiveResponse.PeriodBlock(
                                        periodResult.period(),
                                        entries
                                );
                            })
                            .toList();

                    return new RankingReactiveResponse(blocks);
                });
    }
}