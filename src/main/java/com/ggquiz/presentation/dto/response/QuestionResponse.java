package com.ggquiz.presentation.dto.response;

import java.time.LocalDateTime;

public record QuestionResponse(
        Long id,
        String region,
        String statement,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        String correctOption,
        int difficulty,
        String status,
        LocalDateTime createdAt
) {}