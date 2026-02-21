package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.application.usecases.CreateQuestionUseCase.CreateQuestionCommand;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.enums.CorrectOption;
import com.ggquiz.domain.repositories.QuestionRepository;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateQuestionUseCase {

    private final QuestionRepository questionRepository;
    private final RegionRepository regionRepository;

    public Question execute(Long questionId, CreateQuestionCommand command) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Pergunta não encontrada"));

        Region region = regionRepository.findById(command.regionId())
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));

        question.setRegion(region);
        question.setStatement(command.statement());
        question.setOptionA(command.optionA());
        question.setOptionB(command.optionB());
        question.setOptionC(command.optionC());
        question.setOptionD(command.optionD());
        question.setCorrectOption(CorrectOption.valueOf(command.correctOption().toUpperCase()));
        question.setDifficulty(command.difficulty());

        return questionRepository.save(question);
    }
}