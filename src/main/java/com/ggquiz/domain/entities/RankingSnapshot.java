package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.RankingPeriod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RankingSnapshot {
    private Long id;
    private User user;
    private Region region;
    private RankingPeriod period;
    private GameSession bestSession;
    private BigDecimal bestRating;
    private int totalAttempts;
    private LocalDate periodStart;
    private LocalDateTime updatedAt;
}