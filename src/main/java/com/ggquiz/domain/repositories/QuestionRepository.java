package com.ggquiz.domain.repositories;

import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.enums.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    Question save(Question question);
    Optional<Question> findById(Long id);
    List<Question> findAllById(List<Long> ids);
    void delete(Question question);
    Page<Question> findAllByStatus(QuestionStatus status, Pageable pageable);
    List<Question> findRandomApprovedByRegion(Integer regionId, int limit);
    List<Question> findRandomApproved(int limit);
}