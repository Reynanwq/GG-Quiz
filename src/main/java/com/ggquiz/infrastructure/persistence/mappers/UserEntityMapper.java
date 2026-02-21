package com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.User;
import com.ggquiz.infrastructure.persistence.entities.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toDomain(UserJpaEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .googleId(entity.getGoogleId())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public UserJpaEntity toEntity(User domain) {
        return UserJpaEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .passwordHash(domain.getPasswordHash())
                .googleId(domain.getGoogleId())
                .role(domain.getRole())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}