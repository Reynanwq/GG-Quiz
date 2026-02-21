package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateRegionUseCase {

    private final RegionRepository regionRepository;

    public record CreateRegionCommand(String slug, String name) {}

    public Region execute(CreateRegionCommand command) {
        if (command.slug() == null || command.slug().isBlank()) {
            throw new BusinessRuleException("Slug is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Name is required");
        }

        Region region = Region.builder()
                .slug(command.slug().toLowerCase().trim())
                .name(command.name().trim())
                .active(true)
                .build();

        return regionRepository.save(region);
    }
}