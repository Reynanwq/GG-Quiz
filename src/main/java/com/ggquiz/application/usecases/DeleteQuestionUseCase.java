package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteQuestionUseCase {

    private final QuestionRepository questionRepository;

    public void execute(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Pergunta não encontrada"));
        questionRepository.delete(question);
    }
}