package com.ggquiz.presentation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SessionResponse(
        Long id,
        String username,
        String mode,
        String region,
        int totalCorrect,
        int durationSeconds,
        BigDecimal rating,
        LocalDateTime playedAt
) {}