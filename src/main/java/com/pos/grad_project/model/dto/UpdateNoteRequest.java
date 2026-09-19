package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNoteRequest(
        @NotBlank(message = "note text is required")
        String text
) {}
