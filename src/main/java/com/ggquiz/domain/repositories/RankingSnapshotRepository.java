package com.ggquiz.domain.repositories;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.enums.RankingPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface RankingSnapshotRepository {
    RankingSnapshot save(RankingSnapshot snapshot);
    Optional<RankingSnapshot> findByUserIdAndRegionIdAndPeriodAndPeriodStart(
            Long userId, Integer regionId, RankingPeriod period, LocalDate periodStart);
    Page<RankingSnapshot> findRanking(Integer regionId, RankingPeriod period, LocalDate periodStart, Pageable pageable);

    long countByRatingGreaterThan(BigDecimal rating, Integer regionId, RankingPeriod period, LocalDate periodStart);
}