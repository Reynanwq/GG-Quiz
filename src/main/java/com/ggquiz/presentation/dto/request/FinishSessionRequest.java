package com.ggquiz.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record FinishSessionRequest(
        @NotBlank String mode,
        Integer regionId,
        @NotNull @Positive int durationSeconds,
        @NotNull List<Long> correctQuestionIds,
        Long wrongQuestionId
) {}