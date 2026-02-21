package com.ggquiz.application.config;

import com.ggquiz.application.usecases.*;
import com.ggquiz.domain.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}