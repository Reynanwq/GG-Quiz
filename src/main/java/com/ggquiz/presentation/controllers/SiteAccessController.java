package com.ggquiz.presentation.controllers;

import com.ggquiz.infrastructure.persistence.entities.SiteAccessJpaEntity;
import com.ggquiz.infrastructure.persistence.repositories.SiteAccessJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class SiteAccessController {

    private final SiteAccessJpaRepository repository;

    // Chamado pelo frontend quando o site carrega — incrementa o contador
    @PostMapping("/ping")
    @Transactional
    public void ping() {
        if (!repository.existsById(1L)) {
            repository.save(SiteAccessJpaEntity.builder()
                    .id(1L)
                    .totalAccesses(1L)
                    .build());
        } else {
            repository.increment();
        }
    }

    // Retorna o total — só admins verão isso no painel
    @GetMapping
    public long getTotal() {
        return repository.findById(1L)
                .map(SiteAccessJpaEntity::getTotalAccesses)
                .orElse(0L);
    }
}