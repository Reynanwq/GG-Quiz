package com.ggquiz.presentation.controllers;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.application.usecases.ChangeUserRoleUseCase;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.presentation.dto.response.UserResponse;
import com.ggquiz.presentation.mappers.PresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final UserRepository userRepository;
    private final PresentationMapper mapper;

    // PATCH /api/users/{id}/role?role=ADMIN
    // PATCH /api/users/{id}/role?role=USER
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeRole(
            @PathVariable Long id,
            @RequestParam Role role,
            Authentication auth) {
        User admin = userRepository.findByEmail(auth.getName()).orElseThrow();
        User updated = changeUserRoleUseCase.execute(id, role, admin);
        return mapper.toUserResponse(updated);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findByUsername(@RequestParam String username, Authentication auth) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado."));
        return mapper.toUserResponse(user);
    }
}