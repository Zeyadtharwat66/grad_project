package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Duration;

public record NoteReqDTO(
        @NotBlank(message = "note text is required")
        String text,
        Duration time,
        @PositiveOrZero(message = "lesson id must be positive")
        long lessonId
) {}
