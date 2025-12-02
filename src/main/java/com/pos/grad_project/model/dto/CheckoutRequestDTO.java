package com.pos.grad_project.model.dto;

import java.util.List;

public record CheckoutRequestDTO(List<CoursesCheckOutReqDTO> courses) {
}
