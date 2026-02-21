package com.ggquiz.presentation.controllers;

import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionRepository regionRepository;

    @GetMapping
    public List<Region> listActive() {
        return regionRepository.findAllActive();
    }
}