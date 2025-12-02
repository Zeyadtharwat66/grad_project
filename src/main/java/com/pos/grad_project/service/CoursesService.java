package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.enums.Grade;
import org.springframework.http.ResponseEntity;

public interface CoursesService {
    public ResponseEntity<?> allCourses(CoursesReqDTO coursesReqDTO,int page, int size);
    public ResponseEntity<?> courseContent(Long id);
    public ResponseEntity<?> courseInfo(Long id);
    public ResponseEntity<?> teacherOfCourseInfo(Long id);
    public ResponseEntity<?> courseReview(Long id);
    public ResponseEntity<?> relatedCourses(Long id);
    public ResponseEntity<?> findCoursesByFilters(String grade, Double minPrice,Double maxPrice, Float rate, String name, Long teacher, String category,int page,int size );
    public ResponseEntity<?> searchCourse(String name,int page, int size);
    public ResponseEntity<?> showMaterial(long sectionId);
    public ResponseEntity<?> addNote(String note,long sectionId,long studentId);
    public ResponseEntity<?> updateNote(String note,long noteId,long studentId);
    public ResponseEntity<?> deleteNote(long noteId,long studentId);
    public ResponseEntity<?> getProgress(long studentId, long courseId);
}
