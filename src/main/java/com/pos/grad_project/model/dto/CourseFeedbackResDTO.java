package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.StudentEntity;

public record CourseFeedbackResDTO(Long id,
                                   String comment,
                                   Float rating,
                                   StudentEntity student,
                                   CourseEntity course) {
}
