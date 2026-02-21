package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.GameMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GameSession {
    private Long id;
    private User user;
    private Region region;
    private GameMode mode;
    private int totalCorrect;
    private Question wrongQuestion;
    private int durationSeconds;
    private BigDecimal rating;
    private LocalDateTime playedAt;
}