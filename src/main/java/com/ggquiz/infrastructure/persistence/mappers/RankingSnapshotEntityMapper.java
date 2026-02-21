package com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.infrastructure.persistence.entities.RankingSnapshotJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankingSnapshotEntityMapper {

    private final UserEntityMapper userMapper;
    private final RegionEntityMapper regionMapper;
    private final GameSessionEntityMapper sessionMapper;

    public RankingSnapshot toDomain(RankingSnapshotJpaEntity entity) {
        return RankingSnapshot.builder()
                .id(entity.getId())
                .user(userMapper.toDomain(entity.getUser()))
                .region(entity.getRegion() != null ? regionMapper.toDomain(entity.getRegion()) : null)
                .period(entity.getPeriod())
                .bestSession(sessionMapper.toDomain(entity.getBestSession()))
                .bestRating(entity.getBestRating())
                .totalAttempts(entity.getTotalAttempts())
                .periodStart(entity.getPeriodStart())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RankingSnapshotJpaEntity toEntity(RankingSnapshot domain) {
        return RankingSnapshotJpaEntity.builder()
                .id(domain.getId())
                .user(userMapper.toEntity(domain.getUser()))
                .region(domain.getRegion() != null ? regionMapper.toEntity(domain.getRegion()) : null)
                .period(domain.getPeriod())
                .bestSession(sessionMapper.toEntity(domain.getBestSession()))
                .bestRating(domain.getBestRating())
                .totalAttempts(domain.getTotalAttempts())
                .periodStart(domain.getPeriodStart())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}