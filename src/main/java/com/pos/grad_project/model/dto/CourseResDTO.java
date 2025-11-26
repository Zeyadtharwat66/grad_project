package com.pos.grad_project.model.dto;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.enums.Grade;

import java.awt.print.Pageable;
import java.time.Duration;

public record CourseResDTO(String name, int totalLessons, Grade grade, String description, String language, double price, String imageUrl, float rating,
                           Duration duration, int numberOfStudents) {

}

