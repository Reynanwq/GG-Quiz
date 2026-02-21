package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.domain.repositories.QuestionRepository;
import com.ggquiz.infrastructure.persistence.mappers.QuestionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionJpaRepository jpaRepository;
    private final QuestionEntityMapper mapper;

    @Override
    public Question save(Question question) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(question)));
    }

    @Override
    public Optional<Question> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Question> findAllById(List<Long> ids) {
        return jpaRepository.findAllById(ids).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(Question question) {
        jpaRepository.deleteById(question.getId());
    }

    @Override
    public Page<Question> findAllByStatus(QuestionStatus status, Pageable pageable) {
        return jpaRepository.findAllByStatus(status, pageable).map(mapper::toDomain);
    }

    @Override
    public List<Question> findRandomApprovedByRegion(Integer regionId, int limit) {
        return jpaRepository.findRandomApprovedByRegion(regionId, PageRequest.of(0, limit))
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Question> findRandomApproved(int limit) {
        return jpaRepository.findRandomApproved(PageRequest.of(0, limit))
                .stream().map(mapper::toDomain).toList();
    }
}