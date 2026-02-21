package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.FindMyRankingPositionUseCase;
import com.ggquiz.application.usecases.FindRankingUseCase;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.presentation.dto.response.MyPositionResponse;
import com.ggquiz.presentation.dto.response.RankingResponse;
import com.ggquiz.presentation.mappers.PresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final FindRankingUseCase findRankingUseCase;
    private final FindMyRankingPositionUseCase findMyRankingPositionUseCase;
    private final UserRepository userRepository;
    private final PresentationMapper mapper;

    // GET /api/ranking?period=WEEKLY&regionId=1&page=0&size=10
    @GetMapping
    public Page<RankingResponse> getRanking(@RequestParam(required = false) Integer regionId,
                                            @RequestParam(defaultValue = "ALLTIME") String period,
                                            Pageable pageable) {
        return findRankingUseCase.execute(regionId, period, pageable).map(mapper::toRankingResponse);
    }

    // GET /api/ranking/me?period=ALLTIME&regionId=1
    // Retorna a posição do usuário logado no ranking. 404 se ele não tiver jogado ainda.
    @GetMapping("/me")
    public ResponseEntity<MyPositionResponse> getMyPosition(
            @RequestParam(required = false) Integer regionId,
            @RequestParam(defaultValue = "ALLTIME") String period,
            Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        return findMyRankingPositionUseCase.execute(user, regionId, period)
                .map(r -> ResponseEntity.ok(new MyPositionResponse(
                        r.position(), r.username(), r.bestRating(), r.totalAttempts())))
                .orElse(ResponseEntity.notFound().build());
    }
}