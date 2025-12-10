package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.dto.NoteReqDTO;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

public interface CoursesService {
    public ResponseEntity<?> allCourses(CoursesReqDTO coursesReqDTO,int page, int size);
    public ResponseEntity<?> courseContent(Long id);
    public ResponseEntity<?> courseInfo(Long id);
    public ResponseEntity<?> showVideosPage(long videoId);
    public ResponseEntity<?> teacherOfCourseInfo(Long id);
    public ResponseEntity<?> getCourseReviews(Long id);
    public ResponseEntity<?> relatedCourses(Long id);
    public ResponseEntity<?> addCourseReviews(Long id,String comment,float rate);
    public ResponseEntity<?> findCoursesByFilters(String grade, Double minPrice,Double maxPrice, Float rate, String name, Long teacher, String category,int page,int size );
    public ResponseEntity<?> searchCourse(String name,int page, int size);
    public ResponseEntity<?> showMaterial(long sectionId);
    public ResponseEntity<?> addNote(NoteReqDTO noteReqDTO);
    public ResponseEntity<?> updateNote(String note,long noteId,long studentId);
    public ResponseEntity<?> deleteNote(long noteId,long studentId);
    public ResponseEntity<?> getProgress(long studentId, long courseId);
    public ResponseEntity<?> getNote(long videoId);
    public ResponseEntity<?> completeVideo(long id);
}
