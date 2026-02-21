package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ReviewQuestionUseCase {

    private final QuestionRepository questionRepository;

    public Question approve(Long questionId, User admin) {
        Question question = findPendingOrThrow(questionId);
        question.setStatus(QuestionStatus.APPROVED);
        question.setReviewedBy(admin);
        question.setApprovedAt(LocalDateTime.now());
        return questionRepository.save(question);
    }

    public Question reject(Long questionId, User admin) {
        Question question = findPendingOrThrow(questionId);
        question.setStatus(QuestionStatus.REJECTED);
        question.setReviewedBy(admin);
        return questionRepository.save(question);
    }

    private Question findPendingOrThrow(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Pergunta não encontrada"));

        if (question.getStatus() != QuestionStatus.PENDING) {
            throw new BusinessRuleException("Apenas perguntas pendentes podem ser revisadas");
        }

        return question;
    }
}