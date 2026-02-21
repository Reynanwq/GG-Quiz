package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartGameSessionUseCaseTest {

    @Mock private QuestionRepository questionRepository;

    private StartGameSessionUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new StartGameSessionUseCase(questionRepository);
    }

    @Test
    void shouldReturnQuestions_whenGlobalMode() {
        List<Question> questions = List.of(Question.builder().id(1L).build());
        when(questionRepository.findRandomApproved(10)).thenReturn(questions);

        List<Question> result = useCase.execute("GLOBAL", null);

        assertThat(result).isEqualTo(questions);
        verify(questionRepository).findRandomApproved(10);
        verify(questionRepository, never()).findRandomApprovedByRegion(any(), anyInt());
    }

    @Test
    void shouldReturnQuestions_whenRegionalMode() {
        List<Question> questions = List.of(Question.builder().id(1L).build());
        when(questionRepository.findRandomApprovedByRegion(1, 10)).thenReturn(questions);

        List<Question> result = useCase.execute("REGIONAL", 1);

        assertThat(result).isEqualTo(questions);
        verify(questionRepository).findRandomApprovedByRegion(1, 10);
    }

    @Test
    void shouldThrow_whenRegionalModeWithoutRegionId() {
        assertThatThrownBy(() -> useCase.execute("REGIONAL", null))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("regionId é obrigatório para modo regional");
    }

    @Test
    void shouldBeCaseInsensitive_forMode() {
        List<Question> questions = List.of(Question.builder().id(1L).build());
        when(questionRepository.findRandomApproved(10)).thenReturn(questions);

        List<Question> result = useCase.execute("global", null);

        assertThat(result).isEqualTo(questions);
    }
}