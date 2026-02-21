package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.domain.entities.GameSession;
import com.ggquiz.domain.repositories.GameSessionRepository;
import com.ggquiz.infrastructure.persistence.mappers.GameSessionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameSessionRepositoryImpl implements GameSessionRepository {

    private final GameSessionJpaRepository jpaRepository;
    private final GameSessionEntityMapper mapper;

    @Override
    public GameSession save(GameSession session) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(session)));
    }
}