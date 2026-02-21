package com.ggquiz.domain.repositories;

import com.ggquiz.domain.entities.Region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    List<Region> findAllActive();
    Optional<Region> findById(Integer id);
}