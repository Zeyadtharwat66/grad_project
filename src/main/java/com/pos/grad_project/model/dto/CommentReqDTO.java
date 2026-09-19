package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CommentReqDTO(
        @NotBlank(message = "comment is required") String comment,
        @Min(0) @Max(5) float rate,
        @Positive(message = "lesson id must be positive") long lessonId
) {}
