package com.pos.grad_project.model.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank(message = "username is required")
                       String username,
                       @NotBlank(message = "username is required")
                       String password) {
}
