package com.ggquiz.presentation.mappers;

import com.ggquiz.domain.entities.GameSession;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.RankingSnapshot;
import com.ggquiz.domain.entities.User;
import com.ggquiz.presentation.dto.response.QuestionResponse;
import com.ggquiz.presentation.dto.response.RankingResponse;
import com.ggquiz.presentation.dto.response.SessionResponse;
import com.ggquiz.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class PresentationMapper {

    public QuestionResponse toQuestionResponse(Question q) {
        return new QuestionResponse(
                q.getId(),
                q.getRegion().getSlug(),
                q.getStatement(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD(),
                q.getCorrectOption().name(),
                q.getDifficulty(),
                q.getStatus().name(),
                q.getCreatedAt()
        );
    }

    public SessionResponse toSessionResponse(GameSession s) {
        return new SessionResponse(
                s.getId(),
                s.getUser().getUsername(),
                s.getMode().name(),
                s.getRegion() != null ? s.getRegion().getSlug() : "global",
                s.getTotalCorrect(),
                s.getDurationSeconds(),
                s.getRating(),
                s.getPlayedAt()
        );
    }

    public RankingResponse toRankingResponse(RankingSnapshot s) {
        return new RankingResponse(
                s.getUser().getUsername(),
                s.getBestRating(),
                s.getTotalAttempts(),
                s.getRegion() != null ? s.getRegion().getSlug() : "global"
        );
    }

    public UserResponse toUserResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRole().name()
        );
    }
}