package com.ggquiz.domain.repositories;

import com.ggquiz.domain.entities.GameSession;

public interface GameSessionRepository {
    GameSession save(GameSession session);
}