package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoderPort passwordEncoder;

    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AuthenticateUserUseCase(userRepository, passwordEncoder);
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("player@ggquiz.com")
                .passwordHash("hashed_senha")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail("player@ggquiz.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", "hashed_senha")).thenReturn(true);

        User result = useCase.execute("player@ggquiz.com", "senha123");

        assertThat(result.getEmail()).isEqualTo("player@ggquiz.com");
    }

    @Test
    void shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("naoexiste@ggquiz.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute("naoexiste@ggquiz.com", "senha123"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void shouldThrow_whenPasswordDoesNotMatch() {
        User user = User.builder()
                .email("player@ggquiz.com")
                .passwordHash("hashed_senha")
                .build();

        when(userRepository.findByEmail("player@ggquiz.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha_errada", "hashed_senha")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute("player@ggquiz.com", "senha_errada"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Credenciais inválidas");
    }
}