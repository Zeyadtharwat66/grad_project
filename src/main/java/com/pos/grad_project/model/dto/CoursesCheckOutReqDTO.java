package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CoursesCheckOutReqDTO(
        @NotNull(message = "course id is required")
        @Positive(message = "course id must be positive")
        Long courseId
) {}
