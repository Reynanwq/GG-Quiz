package com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.GameSession;
import com.ggquiz.infrastructure.persistence.entities.GameSessionJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameSessionEntityMapper {

    private final UserEntityMapper userMapper;
    private final RegionEntityMapper regionMapper;
    private final QuestionEntityMapper questionMapper;

    public GameSession toDomain(GameSessionJpaEntity entity) {
        return GameSession.builder()
                .id(entity.getId())
                .user(userMapper.toDomain(entity.getUser()))
                .region(entity.getRegion() != null ? regionMapper.toDomain(entity.getRegion()) : null)
                .mode(entity.getMode())
                .totalCorrect(entity.getTotalCorrect())
                .wrongQuestion(entity.getWrongQuestion() != null ? questionMapper.toDomain(entity.getWrongQuestion()) : null)
                .durationSeconds(entity.getDurationSeconds())
                .rating(entity.getRating())
                .playedAt(entity.getPlayedAt())
                .build();
    }

    public GameSessionJpaEntity toEntity(GameSession domain) {
        return GameSessionJpaEntity.builder()
                .id(domain.getId())
                .user(userMapper.toEntity(domain.getUser()))
                .region(domain.getRegion() != null ? regionMapper.toEntity(domain.getRegion()) : null)
                .mode(domain.getMode())
                .totalCorrect(domain.getTotalCorrect())
                .wrongQuestion(domain.getWrongQuestion() != null ? questionMapper.toEntity(domain.getWrongQuestion()) : null)
                .durationSeconds(domain.getDurationSeconds())
                .rating(domain.getRating())
                .playedAt(domain.getPlayedAt())
                .build();
    }
}