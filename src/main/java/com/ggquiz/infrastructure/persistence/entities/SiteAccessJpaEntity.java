package com.ggquiz.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "site_access")
public class SiteAccessJpaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long totalAccesses;
}