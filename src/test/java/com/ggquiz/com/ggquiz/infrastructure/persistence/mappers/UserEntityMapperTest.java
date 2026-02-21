package com.ggquiz.com.ggquiz.infrastructure.persistence.mappers;

import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.infrastructure.persistence.entities.UserJpaEntity;
import com.ggquiz.infrastructure.persistence.mappers.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserEntityMapper();
    }

    @Test
    void shouldMapEntityToDomain() {
        LocalDateTime now = LocalDateTime.now();
        UserJpaEntity entity = UserJpaEntity.builder()
                .id(1L)
                .username("player1")
                .email("player@ggquiz.com")
                .passwordHash("hash")
                .googleId(null)
                .role(Role.USER)
                .createdAt(now)
                .build();

        User domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getUsername()).isEqualTo("player1");
        assertThat(domain.getEmail()).isEqualTo("player@ggquiz.com");
        assertThat(domain.getPasswordHash()).isEqualTo("hash");
        assertThat(domain.getRole()).isEqualTo(Role.USER);
        assertThat(domain.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldMapDomainToEntity() {
        LocalDateTime now = LocalDateTime.now();
        User domain = User.builder()
                .id(1L)
                .username("player1")
                .email("player@ggquiz.com")
                .passwordHash("hash")
                .role(Role.USER)
                .createdAt(now)
                .build();

        UserJpaEntity entity = mapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getUsername()).isEqualTo("player1");
        assertThat(entity.getEmail()).isEqualTo("player@ggquiz.com");
        assertThat(entity.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void shouldPreserveRoundTrip() {
        UserJpaEntity original = UserJpaEntity.builder()
                .id(1L)
                .username("player1")
                .email("player@ggquiz.com")
                .passwordHash("hash")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        UserJpaEntity result = mapper.toEntity(mapper.toDomain(original));

        assertThat(result.getId()).isEqualTo(original.getId());
        assertThat(result.getUsername()).isEqualTo(original.getUsername());
        assertThat(result.getEmail()).isEqualTo(original.getEmail());
        assertThat(result.getRole()).isEqualTo(original.getRole());
    }
}