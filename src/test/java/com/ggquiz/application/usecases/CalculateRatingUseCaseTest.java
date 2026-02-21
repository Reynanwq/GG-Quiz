package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateRatingUseCaseTest {

    private CalculateRatingUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculateRatingUseCase();
    }

    @Test
    void shouldReturnZero_whenNoCorrectQuestions() {
        BigDecimal rating = useCase.execute(List.of(), questionWithDifficulty(5), 60);
        assertThat(rating).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZero_whenWrongQuestionHasDifficulty1() {
        List<Question> correct = List.of(
                questionWithDifficulty(5),
                questionWithDifficulty(8)
        );
        // fator = (1 - 1) / 10 = 0.0 → rating = 0
        BigDecimal rating = useCase.execute(correct, questionWithDifficulty(1), 60);

        assertThat(rating).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldApplyPenalty_whenWrongQuestionHasDifficulty5() {
        // base = (100 * 5) / 60 = 8.3333
        // fator = (5 - 1) / 10 = 0.4
        // rating = 8.3333 * 0.4 = 3.3333
        List<Question> correct = List.of(questionWithDifficulty(5));
        BigDecimal rating = useCase.execute(correct, questionWithDifficulty(5), 60);

        assertThat(rating).isPositive();
        assertThat(rating).isLessThan(BigDecimal.valueOf(8.5));
    }

    @Test
    void shouldApplyLightPenalty_whenWrongQuestionHasDifficulty10() {
        // fator = (10 - 1) / 10 = 0.9 → preserva 90% do rating base
        List<Question> correct = List.of(questionWithDifficulty(10));
        BigDecimal ratingWithPenalty = useCase.execute(correct, questionWithDifficulty(10), 60);
        BigDecimal ratingWithoutPenalty = useCase.execute(correct, null, 60);

        assertThat(ratingWithPenalty).isLessThan(ratingWithoutPenalty);
        // deve ser 90% do rating base
        BigDecimal expected = ratingWithoutPenalty.multiply(BigDecimal.valueOf(0.9));
        assertThat(ratingWithPenalty).isCloseTo(expected, org.assertj.core.data.Offset.offset(BigDecimal.valueOf(0.001)));
    }

    @Test
    void shouldReturnFullRating_whenNoWrongQuestion() {
        // null wrongQuestion = zerou o quiz, sem penalidade (fator = 1.0)
        List<Question> correct = List.of(
                questionWithDifficulty(7),
                questionWithDifficulty(9)
        );
        BigDecimal rating = useCase.execute(correct, null, 120);

        // base = (100 * 7 + 100 * 9) / 120 = 1600 / 120 = 13.3333
        assertThat(rating).isPositive();
        assertThat(rating).isGreaterThan(BigDecimal.valueOf(13));
    }

    @Test
    void shouldProduceHigherRating_whenFasterTime() {
        List<Question> correct = List.of(questionWithDifficulty(8));

        BigDecimal fastRating = useCase.execute(correct, null, 30);
        BigDecimal slowRating = useCase.execute(correct, null, 120);

        assertThat(fastRating).isGreaterThan(slowRating);
    }

    @Test
    void shouldProduceHigherRating_whenHarderQuestions() {
        List<Question> easyQuestions = List.of(questionWithDifficulty(1), questionWithDifficulty(2));
        List<Question> hardQuestions = List.of(questionWithDifficulty(9), questionWithDifficulty(10));

        BigDecimal easyRating = useCase.execute(easyQuestions, null, 60);
        BigDecimal hardRating = useCase.execute(hardQuestions, null, 60);

        assertThat(hardRating).isGreaterThan(easyRating);
    }

    private Question questionWithDifficulty(int difficulty) {
        return Question.builder().difficulty(difficulty).build();
    }
}