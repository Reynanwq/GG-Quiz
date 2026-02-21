package com.ggquiz.infrastructure.persistence.entities;

import com.ggquiz.domain.enums.RankingPeriod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ranking_snapshots")
public class RankingSnapshotJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionJpaEntity region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RankingPeriod period;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "best_session_id", nullable = false)
    private GameSessionJpaEntity bestSession;

    @Column(name = "best_rating", nullable = false, precision = 10, scale = 4)
    private BigDecimal bestRating;

    @Column(name = "total_attempts", nullable = false)
    private int totalAttempts;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}