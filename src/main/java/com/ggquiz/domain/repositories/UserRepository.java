package com.ggquiz.domain.repositories;

import com.ggquiz.domain.entities.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}