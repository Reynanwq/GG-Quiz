package com.ggquiz.presentation.dto.request;

import jakarta.validation.constraints.*;

public record QuestionRequest(
        @NotNull Integer regionId,
        @NotBlank String statement,
        @NotBlank @Size(max = 300) String optionA,
        @NotBlank @Size(max = 300) String optionB,
        @NotBlank @Size(max = 300) String optionC,
        @NotBlank @Size(max = 300) String optionD,
        @NotBlank @Pattern(regexp = "[aAbBcCdD]") String correctOption,
        @NotNull @Min(1) @Max(10) Integer difficulty
) {}