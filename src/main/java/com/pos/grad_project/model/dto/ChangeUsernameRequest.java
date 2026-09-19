package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUsernameRequest(
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 20, message = "username must be min 3 and max 20")
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z0-9]*$",
                message = "Username must start with a letter and contain only letters and numbers"
        )
        String newUsername
) {}
