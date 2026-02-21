package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.enums.QuestionStatus;
import com.ggquiz.infrastructure.persistence.entities.QuestionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

    Page<QuestionJpaEntity> findAllByStatus(QuestionStatus status, Pageable pageable);

    @Query("SELECT q FROM QuestionJpaEntity q WHERE q.region.id = :regionId AND q.status = 'APPROVED' ORDER BY RAND()")
    List<QuestionJpaEntity> findRandomApprovedByRegion(Integer regionId, Pageable pageable);

    @Query("SELECT q FROM QuestionJpaEntity q WHERE q.status = 'APPROVED' ORDER BY RAND()")
    List<QuestionJpaEntity> findRandomApproved(Pageable pageable);
}