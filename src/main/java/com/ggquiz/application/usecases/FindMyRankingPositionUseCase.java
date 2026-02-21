package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.domain.repositories.RankingSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public class FindMyRankingPositionUseCase {

    private final RankingSnapshotRepository rankingSnapshotRepository;

    public record MyPositionResult(
            long position,
            String username,
            double bestRating,
            int totalAttempts
    ) {}

    public Optional<MyPositionResult> execute(User user, Integer regionId, String period) {
        RankingPeriod rankingPeriod = RankingPeriod.valueOf(period.toUpperCase());
        LocalDate periodStart = resolvePeriodStart(rankingPeriod);

        // Busca o snapshot do usuário para este período/região
        Optional<RankingSnapshot> mySnapshot = rankingSnapshotRepository
                .findByUserIdAndRegionIdAndPeriodAndPeriodStart(
                        user.getId(), regionId, rankingPeriod, periodStart);

        if (mySnapshot.isEmpty()) return Optional.empty();

        // Conta quantos registros têm rating MAIOR que o do usuário = posição dele
        long position = rankingSnapshotRepository
                .countByRatingGreaterThan(mySnapshot.get().getBestRating(), regionId, rankingPeriod, periodStart) + 1;

        return Optional.of(new MyPositionResult(
                position,
                user.getUsername(),
                mySnapshot.get().getBestRating().doubleValue(),
                mySnapshot.get().getTotalAttempts()
        ));
    }

    private LocalDate resolvePeriodStart(RankingPeriod period) {
        LocalDate today = LocalDate.now();
        return switch (period) {
            case DAILY   -> today;
            case WEEKLY  -> today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
            case MONTHLY -> today.with(TemporalAdjusters.firstDayOfMonth());
            case ALLTIME -> LocalDate.of(2000, 1, 1);
        };
    }
}