package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.application.usecases.CreateQuestionUseCase.CreateQuestionCommand;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.AuthorType;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import com.ggquiz.domain.repositories.RegionRepository;
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
class CreateQuestionUseCaseTest {

    @Mock private QuestionRepository questionRepository;
    @Mock private RegionRepository regionRepository;

    private CreateQuestionUseCase useCase;

    private Region region;
    private User author;
    private CreateQuestionCommand command;

    @BeforeEach
    void setUp() {
        useCase = new CreateQuestionUseCase(questionRepository, regionRepository);

        region = Region.builder().id(1).slug("cblol").name("CBLOL").build();
        author = User.builder().id(1L).username("player1").build();
        command = new CreateQuestionCommand(1, "Qual time ganhou?", "LOUD", "paiN", "RED", "Fluxo", "A", 5);
    }

    @Test
    void shouldCreateApprovedQuestion_whenAdmin() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Question result = useCase.execute(command, author, true);

        assertThat(result.getStatus()).isEqualTo(QuestionStatus.APPROVED);
        assertThat(result.getAuthorType()).isEqualTo(AuthorType.SYSTEM);
        assertThat(result.getApprovedAt()).isNotNull();
    }

    @Test
    void shouldCreatePendingQuestion_whenUser() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Question result = useCase.execute(command, author, false);

        assertThat(result.getStatus()).isEqualTo(QuestionStatus.PENDING);
        assertThat(result.getAuthorType()).isEqualTo(AuthorType.USER);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getApprovedAt()).isNull();
    }

    @Test
    void shouldThrow_whenRegionNotFound() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());

        CreateQuestionCommand invalidCommand =
                new CreateQuestionCommand(99, "Pergunta", "A", "B", "C", "D", "A", 5);

        assertThatThrownBy(() -> useCase.execute(invalidCommand, author, false))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Região não encontrada");

        verify(questionRepository, never()).save(any());
    }

    @Test
    void shouldSetCorrectRegion() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Question result = useCase.execute(command, author, false);

        assertThat(result.getRegion().getSlug()).isEqualTo("cblol");
    }
}