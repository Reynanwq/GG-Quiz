package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewQuestionUseCaseTest {

    @Mock private QuestionRepository questionRepository;

    private ReviewQuestionUseCase useCase;
    private User admin;

    @BeforeEach
    void setUp() {
        useCase = new ReviewQuestionUseCase(questionRepository);
        admin = User.builder().id(1L).username("admin").build();
    }

    @Test
    void shouldApproveQuestion_whenPending() {
        Question question = Question.builder()
                .id(1L)
                .status(QuestionStatus.PENDING)
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Question result = useCase.approve(1L, admin);

        assertThat(result.getStatus()).isEqualTo(QuestionStatus.APPROVED);
        assertThat(result.getReviewedBy()).isEqualTo(admin);
        assertThat(result.getApprovedAt()).isNotNull();
    }

    @Test
    void shouldRejectQuestion_whenPending() {
        Question question = Question.builder()
                .id(1L)
                .status(QuestionStatus.PENDING)
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Question result = useCase.reject(1L, admin);

        assertThat(result.getStatus()).isEqualTo(QuestionStatus.REJECTED);
        assertThat(result.getReviewedBy()).isEqualTo(admin);
    }

    @Test
    void shouldThrow_whenQuestionNotFound() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.approve(99L, admin))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrow_whenApprovingAlreadyApprovedQuestion() {
        Question question = Question.builder()
                .id(1L)
                .status(QuestionStatus.APPROVED)
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> useCase.approve(1L, admin))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Apenas perguntas pendentes podem ser revisadas");

        verify(questionRepository, never()).save(any());
    }

    @Test
    void shouldThrow_whenRejectingAlreadyRejectedQuestion() {
        Question question = Question.builder()
                .id(1L)
                .status(QuestionStatus.REJECTED)
                .build();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> useCase.reject(1L, admin))
                .isInstanceOf(BusinessRuleException.class);

        verify(questionRepository, never()).save(any());
    }
}