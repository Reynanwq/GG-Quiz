package com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.Region;
import com.ggquiz.infrastructure.persistence.entities.RegionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RegionEntityMapper {

    public Region toDomain(RegionJpaEntity entity) {
        return Region.builder()
                .id(entity.getId())
                .slug(entity.getSlug())
                .name(entity.getName())
                .active(entity.isActive())
                .build();
    }

    public RegionJpaEntity toEntity(Region domain) {
        return RegionJpaEntity.builder()
                .id(domain.getId())
                .slug(domain.getSlug())
                .name(domain.getName())
                .active(domain.isActive())
                .build();
    }
}