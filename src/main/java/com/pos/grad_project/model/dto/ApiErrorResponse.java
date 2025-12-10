package com.pos.grad_project.model.dto;

import java.util.Map;

public record ApiErrorResponse(
        String message,
        Map<String, String> errors
) {}