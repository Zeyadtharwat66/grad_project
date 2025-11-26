package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.TeacherEntity;

import java.util.List;

public record CoursePageResDTO(CourseEntity course,
                               TeacherEntity teacher) {

}
