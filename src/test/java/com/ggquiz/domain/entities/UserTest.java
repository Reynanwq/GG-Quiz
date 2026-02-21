package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldBuildUserWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .username("player1")
                .email("player1@ggquiz.com")
                .passwordHash("hashed_password")
                .role(Role.USER)
                .createdAt(now)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("player1");
        assertThat(user.getEmail()).isEqualTo("player1@ggquiz.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashed_password");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldAllowAdminRole() {
        User admin = User.builder()
                .username("admin")
                .email("admin@ggquiz.com")
                .role(Role.ADMIN)
                .build();

        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void shouldAllowNullGoogleId_whenEmailLogin() {
        User user = User.builder()
                .username("player1")
                .email("player1@ggquiz.com")
                .passwordHash("hash")
                .googleId(null)
                .role(Role.USER)
                .build();

        assertThat(user.getGoogleId()).isNull();
        assertThat(user.getPasswordHash()).isNotNull();
    }

    @Test
    void shouldAllowNullPasswordHash_whenGoogleLogin() {
        User user = User.builder()
                .username("player1")
                .email("player1@ggquiz.com")
                .passwordHash(null)
                .googleId("google_123")
                .role(Role.USER)
                .build();

        assertThat(user.getPasswordHash()).isNull();
        assertThat(user.getGoogleId()).isEqualTo("google_123");
    }
}