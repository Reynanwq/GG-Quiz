package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.CreateRegionUseCase;
import com.ggquiz.application.usecases.ToggleRegionUseCase;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionRepository regionRepository;
    private final CreateRegionUseCase createRegionUseCase;
    private final ToggleRegionUseCase toggleRegionUseCase;

    @GetMapping
    public List<Region> listActive() {
        return regionRepository.findAllActive();
    }

    @PatchMapping("/{id}/toggle")
    public Region toggle(@PathVariable Integer id) {
        return toggleRegionUseCase.execute(id);
    }

    @GetMapping("/all")
    public List<Region> listAll() {
        return regionRepository.findAll();
    }

    @PostMapping
    public Region create(@RequestBody CreateRegionUseCase.CreateRegionCommand command) {
        return createRegionUseCase.execute(command);
    }
}