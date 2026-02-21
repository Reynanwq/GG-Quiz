package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.infrastructure.persistence.entities.RegionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionJpaRepository extends JpaRepository<RegionJpaEntity, Integer> {
    List<RegionJpaEntity> findAllByActiveTrue();
}