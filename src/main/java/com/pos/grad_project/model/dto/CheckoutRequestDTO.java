package com.pos.grad_project.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutRequestDTO(
        @NotEmpty(message = "At least one course is required")
        List<@Valid CoursesCheckOutReqDTO> courses
) {}
