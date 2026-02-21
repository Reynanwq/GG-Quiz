package com.ggquiz.application.usecases;

import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FindPendingQuestionsUseCase {

    private final QuestionRepository questionRepository;

    public Page<Question> execute(Pageable pageable) {
        return questionRepository.findAllByStatus(QuestionStatus.PENDING, pageable);
    }
}