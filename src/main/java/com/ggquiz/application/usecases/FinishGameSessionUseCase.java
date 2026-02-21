package com.ggquiz.application.usecases;

import com.ggquiz.application.exceptions.NotFoundException;
import com.ggquiz.domain.entities.GameSession;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.Region;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.enums.GameMode;
import com.ggquiz.domain.repositories.GameSessionRepository;
import com.ggquiz.domain.repositories.QuestionRepository;
import com.ggquiz.domain.repositories.RegionRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class FinishGameSessionUseCase {

    private final GameSessionRepository gameSessionRepository;
    private final QuestionRepository questionRepository;
    private final RegionRepository regionRepository;
    private final CalculateRatingUseCase calculateRatingUseCase;
    private final UpdateRankingUseCase updateRankingUseCase;

    public GameSession execute(FinishSessionCommand command, User user) {
        List<Question> correctQuestions = questionRepository.findAllById(command.correctQuestionIds());

        Question wrongQuestion = resolveWrongQuestion(command.wrongQuestionId());

        BigDecimal rating = calculateRatingUseCase.execute(correctQuestions, wrongQuestion, command.durationSeconds());

        Region region = resolveRegion(command.regionId());

        GameSession session = GameSession.builder()
                .user(user)
                .region(region)
                .mode(GameMode.valueOf(command.mode().toUpperCase()))
                .totalCorrect(correctQuestions.size())
                .wrongQuestion(wrongQuestion)
                .durationSeconds(command.durationSeconds())
                .rating(rating)
                .playedAt(LocalDateTime.now())
                .build();

        GameSession saved = gameSessionRepository.save(session);
        updateRankingUseCase.execute(saved);

        return saved;
    }

    private Question resolveWrongQuestion(Long wrongQuestionId) {
        if (wrongQuestionId == null) return null;
        return questionRepository.findById(wrongQuestionId)
                .orElseThrow(() -> new NotFoundException("Pergunta errada não encontrada"));
    }

    private Region resolveRegion(Integer regionId) {
        if (regionId == null) return null;
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Região não encontrada"));
    }

    public record FinishSessionCommand(
            String mode,
            Integer regionId,
            int durationSeconds,
            List<Long> correctQuestionIds,
            Long wrongQuestionId
    ) {}
}