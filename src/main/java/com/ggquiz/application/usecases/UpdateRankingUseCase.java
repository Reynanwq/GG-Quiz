package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.GameSession;
import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.domain.repositories.RankingSnapshotRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public class UpdateRankingUseCase {

    private final RankingSnapshotRepository rankingSnapshotRepository;

    public void execute(GameSession session) {
        Arrays.stream(RankingPeriod.values()).forEach(period -> upsert(session, period));
    }

    private void upsert(GameSession session, RankingPeriod period) {
        Integer regionId = session.getRegion() != null ? session.getRegion().getId() : null;
        LocalDate periodStart = resolvePeriodStart(period);

        Optional<RankingSnapshot> existing = rankingSnapshotRepository
                .findByUserIdAndRegionIdAndPeriodAndPeriodStart(
                        session.getUser().getId(), regionId, period, periodStart);

        if (existing.isPresent()) {
            RankingSnapshot snapshot = existing.get();
            snapshot.setTotalAttempts(snapshot.getTotalAttempts() + 1);
            snapshot.setUpdatedAt(LocalDateTime.now());

            if (session.getRating().compareTo(snapshot.getBestRating()) > 0) {
                snapshot.setBestRating(session.getRating());
                snapshot.setBestSession(session);
            }

            rankingSnapshotRepository.save(snapshot);
        } else {
            RankingSnapshot snapshot = RankingSnapshot.builder()
                    .user(session.getUser())
                    .region(session.getRegion())
                    .period(period)
                    .bestSession(session)
                    .bestRating(session.getRating())
                    .totalAttempts(1)
                    .periodStart(periodStart)
                    .updatedAt(LocalDateTime.now())
                    .build();

            rankingSnapshotRepository.save(snapshot);
        }
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