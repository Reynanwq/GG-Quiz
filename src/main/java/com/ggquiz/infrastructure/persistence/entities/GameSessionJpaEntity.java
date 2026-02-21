package com.ggquiz.infrastructure.persistence.entities;

import com.ggquiz.domain.enums.GameMode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_sessions")
public class GameSessionJpaEntity {

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
    private GameMode mode;

    @Column(name = "total_correct", nullable = false)
    private int totalCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrong_question_id")
    private QuestionJpaEntity wrongQuestion;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal rating;

    @Column(name = "played_at", nullable = false, updatable = false)
    private LocalDateTime playedAt;
}