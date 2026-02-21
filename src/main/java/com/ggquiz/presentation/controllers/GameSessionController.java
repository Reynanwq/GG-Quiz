package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.FinishGameSessionUseCase;
import com.ggquiz.application.usecases.FinishGameSessionUseCase.FinishSessionCommand;
import com.ggquiz.application.usecases.StartGameSessionUseCase;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.presentation.dto.request.FinishSessionRequest;
import com.ggquiz.presentation.dto.response.QuestionResponse;
import com.ggquiz.presentation.dto.response.SessionResponse;
import com.ggquiz.presentation.mappers.PresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class GameSessionController {

    private final StartGameSessionUseCase startGameSessionUseCase;
    private final FinishGameSessionUseCase finishGameSessionUseCase;
    private final UserRepository userRepository;
    private final PresentationMapper mapper;

    @GetMapping("/start")
    public List<QuestionResponse> start(@RequestParam String mode,
                                        @RequestParam(required = false) Integer regionId) {
        return startGameSessionUseCase.execute(mode, regionId)
                .stream()
                .map(mapper::toQuestionResponse)
                .toList();
    }

    @PostMapping("/finish")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse finish(@Valid @RequestBody FinishSessionRequest request, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        FinishSessionCommand command = new FinishSessionCommand(
                request.mode(),
                request.regionId(),
                request.durationSeconds(),
                request.correctQuestionIds(),
                request.wrongQuestionId()
        );

        return mapper.toSessionResponse(finishGameSessionUseCase.execute(command, user));
    }
}