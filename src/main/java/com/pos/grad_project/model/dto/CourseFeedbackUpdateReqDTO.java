package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseFeedbackUpdateReqDTO(
        @NotNull(message = "feedback id is required") Long feedbackID,
        @NotBlank(message = "comment is required") String comment,
        @Min(0) @Max(5) float rating,
        Long courseId,
        Long studentId
) {}
