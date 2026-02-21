package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.Question;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculateRatingUseCase {

    private static final BigDecimal POINTS_PER_QUESTION = BigDecimal.valueOf(100);
    private static final BigDecimal MIN_FACTOR = BigDecimal.valueOf(0.20);

    public BigDecimal execute(List<Question> correctQuestions, Question wrongQuestion, int durationSeconds) {
        if (correctQuestions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal ratingBase    = calculateBase(correctQuestions, durationSeconds);
        BigDecimal volumeBonus   = calculateVolumeBonus(correctQuestions.size());
        BigDecimal penaltyFactor = calculatePenaltyFactor(wrongQuestion);

        return ratingBase
                .multiply(volumeBonus)
                .multiply(penaltyFactor)
                .setScale(4, RoundingMode.HALF_UP);
    }

    // base = (Σ dificuldade × 100) ÷ tempo
    private BigDecimal calculateBase(List<Question> correctQuestions, int durationSeconds) {
        int sumDifficulty = correctQuestions.stream().mapToInt(Question::getDifficulty).sum();
        BigDecimal totalPoints = POINTS_PER_QUESTION.multiply(BigDecimal.valueOf(sumDifficulty));
        return totalPoints.divide(BigDecimal.valueOf(durationSeconds), 4, RoundingMode.HALF_UP);
    }

    // volumeBonus = 1 + (acertos - 1) × 0.15
    // 1 acerto  → ×1.00  (sem bônus)
    // 2 acertos → ×1.15
    // 5 acertos → ×1.60
    // 8 acertos → ×2.05
    // 10 acertos→ ×2.35
    // Premia linearmente quem responde mais questões, independente do tempo
    private BigDecimal calculateVolumeBonus(int correctCount) {
        double bonus = 1.0 + (correctCount - 1) * 0.15;
        return BigDecimal.valueOf(bonus).setScale(4, RoundingMode.HALF_UP);
    }

    // penalidade = 0.2 + (0.8 × (dificuldade_errada − 1) ÷ 9)
    // dif. 1  → 0.20 → perde 80%
    // dif. 5  → 0.55 → perde 45%
    // dif. 10 → 1.00 → sem penalidade
    // null    → 1.00 → zerou todas, sem penalidade
    private BigDecimal calculatePenaltyFactor(Question wrongQuestion) {
        if (wrongQuestion == null) {
            return BigDecimal.ONE;
        }
        int diff = wrongQuestion.getDifficulty();
        BigDecimal scaledPenalty = BigDecimal.valueOf(0.8)
                .multiply(BigDecimal.valueOf(diff - 1))
                .divide(BigDecimal.valueOf(9), 4, RoundingMode.HALF_UP);
        return MIN_FACTOR.add(scaledPenalty).setScale(4, RoundingMode.HALF_UP);
    }
}