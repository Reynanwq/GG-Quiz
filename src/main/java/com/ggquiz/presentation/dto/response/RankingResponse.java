package com.ggquiz.presentation.dto.response;

import java.math.BigDecimal;

public record RankingResponse(
        String username,
        BigDecimal bestRating,
        int totalAttempts,
        String region
) {}