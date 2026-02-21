package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToggleRegionUseCase {

    private final RegionRepository regionRepository;

    public Region execute(Integer id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Region not found with id: " + id));

        region.setActive(!region.isActive());
        return regionRepository.save(region);
    }
}