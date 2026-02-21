package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.infrastructure.persistence.entities.GameSessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionJpaRepository extends JpaRepository<GameSessionJpaEntity, Long> {}