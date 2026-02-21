package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.AuthorType;
import com.ggquiz.domain.enums.CorrectOption;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateQuestionUseCase {

    private final QuestionRepository questionRepository;
    private final RegionRepository regionRepository;

    public Question execute(CreateQuestionCommand command, User author, boolean isAdmin) {
        Region region = regionRepository.findById(command.regionId())
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));

        Question question = Question.builder()
                .region(region)
                .statement(command.statement())
                .optionA(command.optionA())
                .optionB(command.optionB())
                .optionC(command.optionC())
                .optionD(command.optionD())
                .correctOption(CorrectOption.valueOf(command.correctOption().toUpperCase()))
                .difficulty(command.difficulty())
                .createdAt(LocalDateTime.now())
                .build();

        if (isAdmin) {
            question.setAuthorType(AuthorType.SYSTEM);
            question.setStatus(QuestionStatus.APPROVED);
            question.setApprovedAt(LocalDateTime.now());
        } else {
            question.setAuthor(author);
            question.setAuthorType(AuthorType.USER);
            question.setStatus(QuestionStatus.PENDING);
        }

        return questionRepository.save(question);
    }

    public record CreateQuestionCommand(
            Integer regionId,
            String statement,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctOption,
            int difficulty
    ) {}
}