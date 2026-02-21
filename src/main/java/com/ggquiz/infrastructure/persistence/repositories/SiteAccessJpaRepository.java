package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.infrastructure.persistence.entities.SiteAccessJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SiteAccessJpaRepository extends JpaRepository<SiteAccessJpaEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE SiteAccessJpaEntity s SET s.totalAccesses = s.totalAccesses + 1 WHERE s.id = 1")
    void increment();
}