package com.ggquiz.infrastructure.persistence.repositories;

import com.ggquiz.infrastructure.persistence.entities.PasswordResetTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenJpaEntity, Long> {
    Optional<PasswordResetTokenJpaEntity> findByToken(String token);

    @Transactional
    void deleteByEmail(String email);
}