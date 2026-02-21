package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.domain.repositories.RankingSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RequiredArgsConstructor
public class FindRankingUseCase {

    private final RankingSnapshotRepository rankingSnapshotRepository;

    public Page<RankingSnapshot> execute(Integer regionId, String period, Pageable pageable) {
        RankingPeriod rankingPeriod = RankingPeriod.valueOf(period.toUpperCase());
        LocalDate periodStart = resolvePeriodStart(rankingPeriod);
        return rankingSnapshotRepository.findRanking(regionId, rankingPeriod, periodStart, pageable);
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