package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public User execute(String username, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email já cadastrado");
        }
        if (userRepository.existsByUsername(username)) {
            throw new BusinessRuleException("Username já em uso");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}