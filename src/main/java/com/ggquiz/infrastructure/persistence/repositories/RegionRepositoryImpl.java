package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.repositories.RegionRepository;
import com.ggquiz.infrastructure.persistence.mappers.RegionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepository {

    private final RegionJpaRepository jpaRepository;
    private final RegionEntityMapper mapper;

    @Override
    public List<Region> findAllActive() {
        return jpaRepository.findAllByActiveTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Region> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}