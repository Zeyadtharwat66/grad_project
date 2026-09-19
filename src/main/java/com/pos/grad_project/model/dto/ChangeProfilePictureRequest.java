package com.pos.grad_project.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeProfilePictureRequest(
        @NotBlank(message = "profile picture URL is required")
        String profilePicture
) {}
