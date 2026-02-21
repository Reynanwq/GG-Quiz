package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteQuestionUseCaseTest {

    @Mock private QuestionRepository questionRepository;

    private DeleteQuestionUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteQuestionUseCase(questionRepository);
    }

    @Test
    void shouldDeleteQuestion_whenExists() {
        Question question = Question.builder().id(1L).build();
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        useCase.execute(1L);

        verify(questionRepository).delete(question);
    }

    @Test
    void shouldThrow_whenQuestionNotFound() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Pergunta não encontrada");

        verify(questionRepository, never()).delete(any());
    }
}