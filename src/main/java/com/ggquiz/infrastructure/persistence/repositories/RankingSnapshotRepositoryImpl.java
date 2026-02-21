package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.enums.RankingPeriod;
import com.ggquiz.domain.repositories.RankingSnapshotRepository;
import com.ggquiz.infrastructure.persistence.mappers.RankingSnapshotEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RankingSnapshotRepositoryImpl implements RankingSnapshotRepository {

    private final RankingSnapshotJpaRepository jpaRepository;
    private final RankingSnapshotEntityMapper mapper;

    @Override
    public RankingSnapshot save(RankingSnapshot snapshot) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(snapshot)));
    }

    @Override
    public Optional<RankingSnapshot> findByUserIdAndRegionIdAndPeriodAndPeriodStart(
            Long userId, Integer regionId, RankingPeriod period, LocalDate periodStart) {
        return jpaRepository
                .findByUserIdAndRegionIdAndPeriodAndPeriodStart(userId, regionId, period, periodStart)
                .map(mapper::toDomain);
    }

    @Override
    public Page<RankingSnapshot> findRanking(Integer regionId, RankingPeriod period, LocalDate periodStart, Pageable pageable) {
        return jpaRepository
                .findAllByRegionIdAndPeriodAndPeriodStartOrderByBestRatingDesc(regionId, period, periodStart, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public long countByRatingGreaterThan(BigDecimal rating, Integer regionId, RankingPeriod period, LocalDate periodStart) {
        return jpaRepository.countByRatingGreaterThan(rating, regionId, period, periodStart);
    }
}