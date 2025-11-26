package com.pos.grad_project.model.dto;

public record CourseFeedbackUpdateReqDTO(Long feedbackID,String comment, float rating, Long courseId, Long studentId) {
}
