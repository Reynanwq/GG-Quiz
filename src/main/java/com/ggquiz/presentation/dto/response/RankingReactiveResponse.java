package com.ggquiz.presentation.dto.response;

import com.ggquiz.domain.enums.RankingPeriod;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de resposta do endpoint reativo GET /api/ranking/reactive
 *
 * Retorna todos os períodos de uma vez, cada um com sua lista de entradas.
 */
public record RankingReactiveResponse(
        List<PeriodBlock> periods
) {
    /**
     * Bloco de um período específico (DAILY, WEEKLY, etc.)
     */
    public record PeriodBlock(
            RankingPeriod period,
            List<RankingEntry> entries
    ) {}

    /**
     * Uma linha do ranking.
     */
    public record RankingEntry(
            int position,
            String username,
            BigDecimal bestRating,
            int totalAttempts
    ) {}
}