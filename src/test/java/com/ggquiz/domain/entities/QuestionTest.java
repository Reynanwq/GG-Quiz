package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.AuthorType;
import com.ggquiz.domain.enums.CorrectOption;
import com.ggquiz.domain.enums.QuestionStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    void shouldBuildQuestionWithAllFields() {
        Region region = Region.builder().id(1).slug("cblol").name("CBLOL").build();

        Question question = Question.builder()
                .id(1L)
                .region(region)
                .statement("Qual time venceu o CBLOL 2023?")
                .optionA("LOUD")
                .optionB("paiN")
                .optionC("RED Canids")
                .optionD("Fluxo")
                .correctOption(CorrectOption.A)
                .difficulty(5)
                .authorType(AuthorType.SYSTEM)
                .status(QuestionStatus.APPROVED)
                .build();

        assertThat(question.getId()).isEqualTo(1L);
        assertThat(question.getRegion().getSlug()).isEqualTo("cblol");
        assertThat(question.getCorrectOption()).isEqualTo(CorrectOption.A);
        assertThat(question.getDifficulty()).isEqualTo(5);
        assertThat(question.getStatus()).isEqualTo(QuestionStatus.APPROVED);
    }

    @Test
    void shouldDefaultStatusToPending_whenUserSuggests() {
        Question question = Question.builder()
                .statement("Qual time venceu o CBLOL 2023?")
                .authorType(AuthorType.USER)
                .status(QuestionStatus.PENDING)
                .build();

        assertThat(question.getStatus()).isEqualTo(QuestionStatus.PENDING);
        assertThat(question.getAuthorType()).isEqualTo(AuthorType.USER);
    }

    @Test
    void shouldHaveDifficultyBetween1And10() {
        for (int difficulty = 1; difficulty <= 10; difficulty++) {
            Question question = Question.builder().difficulty(difficulty).build();
            assertThat(question.getDifficulty()).isBetween(1, 10);
        }
    }

    @Test
    void shouldAllowNullAuthor_whenCreatedBySystem() {
        Question question = Question.builder()
                .authorType(AuthorType.SYSTEM)
                .author(null)
                .status(QuestionStatus.APPROVED)
                .build();

        assertThat(question.getAuthor()).isNull();
        assertThat(question.getAuthorType()).isEqualTo(AuthorType.SYSTEM);
    }
}