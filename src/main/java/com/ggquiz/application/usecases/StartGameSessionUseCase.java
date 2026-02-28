package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StartGameSessionUseCase {

    private static final int QUESTIONS_PER_SESSION = 100;

    private final QuestionRepository questionRepository;

    public List<Question> execute(String mode, Integer regionId) {
        if ("GLOBAL".equalsIgnoreCase(mode)) {
            return questionRepository.findRandomApproved(QUESTIONS_PER_SESSION);
        }

        if (regionId == null) {
            throw new BusinessRuleException("regionId é obrigatório para modo regional");
        }

        return questionRepository.findRandomApprovedByRegion(regionId, QUESTIONS_PER_SESSION);
    }
}