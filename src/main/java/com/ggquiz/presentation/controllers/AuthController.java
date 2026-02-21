package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.AuthenticateUserUseCase;
import com.ggquiz.application.usecases.RegisterUserUseCase;
import com.ggquiz.application.usecases.ResetPasswordUseCase;
import com.ggquiz.domain.entities.User;
import com.ggquiz.infrastructure.security.JwtService;
import com.ggquiz.presentation.dto.request.ForgotPasswordRequest;
import com.ggquiz.presentation.dto.request.LoginRequest;
import com.ggquiz.presentation.dto.request.RegisterRequest;
import com.ggquiz.presentation.dto.request.ResetPasswordRequest;
import com.ggquiz.presentation.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = registerUserUseCase.execute(request.username(), request.email(), request.password());
        return buildAuthResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        User user = authenticateUserUseCase.execute(request.email(), request.password());
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        resetPasswordUseCase.sendResetLink(request.email());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.resetPassword(request.token(), request.newPassword());
    }
}