package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseFeedbackReqDTO(
        @NotBlank(message = "comment is required") String comment,
        @Min(0) @Max(5) float rating,
        @NotNull(message = "course id is required") Long courseId,
        Long studentId
) {}
