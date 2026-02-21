package com.ggquiz.presentation.controllers;

import com.ggquiz.application.usecases.*;
import com.ggquiz.application.usecases.CreateQuestionUseCase.CreateQuestionCommand;
import com.ggquiz.domain.entities.Question;
import com.ggquiz.domain.entities.User;
import com.ggquiz.domain.repositories.UserRepository;
import com.ggquiz.presentation.dto.request.QuestionRequest;
import com.ggquiz.presentation.dto.response.QuestionResponse;
import com.ggquiz.presentation.mappers.PresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final CreateQuestionUseCase createQuestionUseCase;
    private final ReviewQuestionUseCase reviewQuestionUseCase;
    private final UpdateQuestionUseCase updateQuestionUseCase;
    private final DeleteQuestionUseCase deleteQuestionUseCase;
    private final FindPendingQuestionsUseCase findPendingQuestionsUseCase;
    private final UserRepository userRepository;
    private final PresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse create(@Valid @RequestBody QuestionRequest request, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        User author = userRepository.findByEmail(auth.getName()).orElseThrow();
        CreateQuestionCommand command = toCommand(request);
        Question question = createQuestionUseCase.execute(command, author, isAdmin);

        return mapper.toQuestionResponse(question);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<QuestionResponse> listPending(Pageable pageable) {
        return findPendingQuestionsUseCase.execute(pageable).map(mapper::toQuestionResponse);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse approve(@PathVariable Long id, Authentication auth) {
        User admin = userRepository.findByEmail(auth.getName()).orElseThrow();
        return mapper.toQuestionResponse(reviewQuestionUseCase.approve(id, admin));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse reject(@PathVariable Long id, Authentication auth) {
        User admin = userRepository.findByEmail(auth.getName()).orElseThrow();
        return mapper.toQuestionResponse(reviewQuestionUseCase.reject(id, admin));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse update(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return mapper.toQuestionResponse(updateQuestionUseCase.execute(id, toCommand(request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deleteQuestionUseCase.execute(id);
    }

    private CreateQuestionCommand toCommand(QuestionRequest request) {
        return new CreateQuestionCommand(
                request.regionId(),
                request.statement(),
                request.optionA(),
                request.optionB(),
                request.optionC(),
                request.optionD(),
                request.correctOption(),
                request.difficulty()
        );
    }
}