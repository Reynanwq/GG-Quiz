package com.ggquiz.application.config;

import com.ggquiz.application.usecases.*;
import com.ggquiz.domain.repositories.*;
import com.ggquiz.infrastructure.persistence.repositories.PasswordResetTokenJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   PasswordEncoderPort passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository,
                                                           PasswordEncoderPort passwordEncoder) {
        return new AuthenticateUserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public ChangeUserRoleUseCase changeUserRoleUseCase(UserRepository userRepository) {
        return new ChangeUserRoleUseCase(userRepository);
    }

    @Bean
    public CreateQuestionUseCase createQuestionUseCase(QuestionRepository questionRepository,
                                                       RegionRepository regionRepository) {
        return new CreateQuestionUseCase(questionRepository, regionRepository);
    }

    @Bean
    public ReviewQuestionUseCase reviewQuestionUseCase(QuestionRepository questionRepository) {
        return new ReviewQuestionUseCase(questionRepository);
    }

    @Bean
    public UpdateQuestionUseCase updateQuestionUseCase(QuestionRepository questionRepository,
                                                       RegionRepository regionRepository) {
        return new UpdateQuestionUseCase(questionRepository, regionRepository);
    }

    @Bean
    public DeleteQuestionUseCase deleteQuestionUseCase(QuestionRepository questionRepository) {
        return new DeleteQuestionUseCase(questionRepository);
    }

    @Bean
    public FindPendingQuestionsUseCase findPendingQuestionsUseCase(QuestionRepository questionRepository) {
        return new FindPendingQuestionsUseCase(questionRepository);
    }

    @Bean
    public StartGameSessionUseCase startGameSessionUseCase(QuestionRepository questionRepository) {
        return new StartGameSessionUseCase(questionRepository);
    }

    @Bean
    public CalculateRatingUseCase calculateRatingUseCase() {
        return new CalculateRatingUseCase();
    }

    @Bean
    public UpdateRankingUseCase updateRankingUseCase(RankingSnapshotRepository rankingSnapshotRepository) {
        return new UpdateRankingUseCase(rankingSnapshotRepository);
    }

    @Bean
    public FinishGameSessionUseCase finishGameSessionUseCase(GameSessionRepository gameSessionRepository,
                                                             QuestionRepository questionRepository,
                                                             RegionRepository regionRepository,
                                                             CalculateRatingUseCase calculateRatingUseCase,
                                                             UpdateRankingUseCase updateRankingUseCase) {
        return new FinishGameSessionUseCase(gameSessionRepository, questionRepository,
                regionRepository, calculateRatingUseCase, updateRankingUseCase);
    }

    @Bean
    public FindRankingUseCase findRankingUseCase(RankingSnapshotRepository rankingSnapshotRepository) {
        return new FindRankingUseCase(rankingSnapshotRepository);
    }

    @Bean
    public FindMyRankingPositionUseCase findMyRankingPositionUseCase(RankingSnapshotRepository rankingSnapshotRepository) {
        return new FindMyRankingPositionUseCase(rankingSnapshotRepository);
    }

    @Bean
    public CreateRegionUseCase createRegionUseCase(RegionRepository regionRepository) {
        return new CreateRegionUseCase(regionRepository);
    }

    @Bean
    public ToggleRegionUseCase toggleRegionUseCase(RegionRepository regionRepository) {
        return new ToggleRegionUseCase(regionRepository);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            UserRepository userRepository,
            PasswordEncoderPort passwordEncoder,
            PasswordResetTokenJpaRepository tokenRepository,
            JavaMailSender mailSender,
            @Value("${app.frontend-url}") String frontendUrl) {
        return new ResetPasswordUseCase(userRepository, passwordEncoder, tokenRepository, mailSender, frontendUrl);
    }

    @Bean
    public FindRankingReactiveUseCase findRankingReactiveUseCase(RankingSnapshotRepository rankingSnapshotRepository) {
        return new FindRankingReactiveUseCase(rankingSnapshotRepository);
    }
}