package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.BusinessRuleException;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.Role;
import com.ggquiz.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangeUserRoleUseCase {

    private final UserRepository userRepository;

    public User execute(Long targetUserId, Role newRole, User requestingAdmin) {
        if (requestingAdmin.getId().equals(targetUserId)) {
            throw new BusinessRuleException("Você não pode alterar seu próprio cargo.");
        }

        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado."));

        target.setRole(newRole);
        return userRepository.save(target);
    }
}