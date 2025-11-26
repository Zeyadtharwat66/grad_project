package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.entity.CategoryEntity;
import com.pos.grad_project.model.enums.Grade;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface CoursesService {
    public ResponseEntity<?> allCourses(CoursesReqDTO coursesReqDTO,int page, int size);
    public ResponseEntity<?> courseContent(Long id);
    public ResponseEntity<?> courseInfo(Long id);
    public ResponseEntity<?> teacherOfCourseInfo(Long id);
    public ResponseEntity<?> courseReview(Long id);
    public ResponseEntity<?> relatedCourses(Long id);
    public ResponseEntity<?> findCoursesByFilters(Grade grade, double minPrice,double maxPrice, float rate, String name, String teacher, String category,int page,int size );
    public ResponseEntity<?> searchCourse(String name,int page, int size);
}
