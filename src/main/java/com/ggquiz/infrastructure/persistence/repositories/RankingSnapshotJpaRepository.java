package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.infrastructure.persistence.entities.RankingSnapshotJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface RankingSnapshotJpaRepository extends JpaRepository<RankingSnapshotJpaEntity, Long> {

    Optional<RankingSnapshotJpaEntity> findByUserIdAndRegionIdAndPeriodAndPeriodStart(
            Long userId, Integer regionId, RankingPeriod period, LocalDate periodStart);

    Page<RankingSnapshotJpaEntity> findAllByRegionIdAndPeriodAndPeriodStartOrderByBestRatingDesc(
            Integer regionId, RankingPeriod period, LocalDate periodStart, Pageable pageable);

    // Conta quantos snapshots têm rating MAIOR que o do usuário informado
    // para determinar a posição dele no ranking (posição = count + 1)
    @Query("""
            SELECT COUNT(r) FROM RankingSnapshotJpaEntity r
            WHERE r.bestRating > :rating
              AND r.period = :period
              AND r.periodStart = :periodStart
              AND (:regionId IS NULL AND r.region IS NULL
                   OR r.region.id = :regionId)
            """)
    long countByRatingGreaterThan(
            @Param("rating") BigDecimal rating,
            @Param("regionId") Integer regionId,
            @Param("period") RankingPeriod period,
            @Param("periodStart") LocalDate periodStart);
}