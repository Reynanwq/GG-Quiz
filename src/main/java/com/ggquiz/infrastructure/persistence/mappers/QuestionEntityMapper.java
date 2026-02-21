package com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.Question;
import com.ggquiz.infrastructure.persistence.entities.QuestionJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionEntityMapper {

    private final RegionEntityMapper regionMapper;
    private final UserEntityMapper userMapper;

    public Question toDomain(QuestionJpaEntity entity) {
        return Question.builder()
                .id(entity.getId())
                .region(regionMapper.toDomain(entity.getRegion()))
                .author(entity.getAuthor() != null ? userMapper.toDomain(entity.getAuthor()) : null)
                .reviewedBy(entity.getReviewedBy() != null ? userMapper.toDomain(entity.getReviewedBy()) : null)
                .statement(entity.getStatement())
                .optionA(entity.getOptionA())
                .optionB(entity.getOptionB())
                .optionC(entity.getOptionC())
                .optionD(entity.getOptionD())
                .correctOption(entity.getCorrectOption())
                .difficulty(entity.getDifficulty())
                .authorType(entity.getAuthorType())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .approvedAt(entity.getApprovedAt())
                .build();
    }

    public QuestionJpaEntity toEntity(Question domain) {
        return QuestionJpaEntity.builder()
                .id(domain.getId())
                .region(regionMapper.toEntity(domain.getRegion()))
                .author(domain.getAuthor() != null ? userMapper.toEntity(domain.getAuthor()) : null)
                .reviewedBy(domain.getReviewedBy() != null ? userMapper.toEntity(domain.getReviewedBy()) : null)
                .statement(domain.getStatement())
                .optionA(domain.getOptionA())
                .optionB(domain.getOptionB())
                .optionC(domain.getOptionC())
                .optionD(domain.getOptionD())
                .correctOption(domain.getCorrectOption())
                .difficulty(domain.getDifficulty())
                .authorType(domain.getAuthorType())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .approvedAt(domain.getApprovedAt())
                .build();
    }
}