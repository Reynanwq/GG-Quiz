package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String googleId;
    private Role role;
    private LocalDateTime createdAt;
}