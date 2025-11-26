package com.pos.grad_project.model.dto;

public record CourseFeedbackReqDTO(String comment,float rating,Long courseId,Long studentId) {
}
