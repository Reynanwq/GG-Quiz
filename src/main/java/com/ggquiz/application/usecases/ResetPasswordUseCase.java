package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.infrastructure.persistence.entities.PasswordResetTokenJpaEntity;
import com.ggquiz.infrastructure.persistence.repositories.PasswordResetTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final PasswordResetTokenJpaRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final String frontendUrl; // sem @Value — recebe pelo construtor

    public void sendResetLink(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            tokenRepository.deleteByEmail(email);

            String token = UUID.randomUUID().toString();

            PasswordResetTokenJpaEntity entity = PasswordResetTokenJpaEntity.builder()
                    .token(token)
                    .email(email)
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .used(false)
                    .build();

            tokenRepository.save(entity);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("GG QUIZ — Recuperação de Senha");
            message.setText(
                    "Olá, " + user.getUsername() + "!\n\n" +
                            "Recebemos uma solicitação para redefinir sua senha.\n\n" +
                            "Clique no link abaixo (válido por 30 minutos):\n" +
                            frontendUrl + "/?token=" + token + "\n\n" +
                            "Se não foi você, ignore este email.\n\n" +
                            "— GG QUIZ"
            );
            mailSender.send(message);
        });
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenJpaEntity entity = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessRuleException("Token inválido ou expirado."));

        if (entity.isUsed()) {
            throw new BusinessRuleException("Este link já foi utilizado.");
        }
        if (entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Este link expirou. Solicite um novo.");
        }

        User user = userRepository.findByEmail(entity.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        entity.setUsed(true);
        tokenRepository.save(entity);
    }
}