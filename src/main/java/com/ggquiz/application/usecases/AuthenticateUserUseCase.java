package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public User execute(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BusinessRuleException("Credenciais inválidas");
        }

        return user;
    }
}