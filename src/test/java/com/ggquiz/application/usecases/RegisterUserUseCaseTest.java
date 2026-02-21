package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoderPort passwordEncoder;

    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail("player@ggquiz.com")).thenReturn(false);
        when(userRepository.existsByUsername("player1")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("hashed_senha123");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = useCase.execute("player1", "player@ggquiz.com", "senha123");

        assertThat(result.getUsername()).isEqualTo("player1");
        assertThat(result.getEmail()).isEqualTo("player@ggquiz.com");
        assertThat(result.getPasswordHash()).isEqualTo("hashed_senha123");
        assertThat(result.getRole()).isEqualTo(Role.USER);
        assertThat(result.getCreatedAt()).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrow_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail("player@ggquiz.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("player1", "player@ggquiz.com", "senha123"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Email já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrow_whenUsernameAlreadyExists() {
        when(userRepository.existsByEmail("player@ggquiz.com")).thenReturn(false);
        when(userRepository.existsByUsername("player1")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute("player1", "player@ggquiz.com", "senha123"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Username já em uso");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldEncodePassword_beforeSaving() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("bcrypt_hash");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        useCase.execute("player1", "player@ggquiz.com", "senha123");

        verify(passwordEncoder).encode("senha123");
    }
}