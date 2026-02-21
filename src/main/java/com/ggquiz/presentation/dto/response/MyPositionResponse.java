package com.ggquiz.presentation.dto.response;

public record MyPositionResponse(
        long position,
        String username,
        double bestRating,
        int totalAttempts
) {}