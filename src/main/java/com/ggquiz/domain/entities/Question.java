package com.ggquiz.domain.entities;

import com.ggquiz.domain.enums.AuthorType;
import com.ggquiz.domain.enums.CorrectOption;
import com.ggquiz.domain.enums.QuestionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Question {
    private Long id;
    private Region region;
    private User author;
    private User reviewedBy;
    private String statement;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private CorrectOption correctOption;
    private int difficulty;
    private AuthorType authorType;
    private QuestionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
}