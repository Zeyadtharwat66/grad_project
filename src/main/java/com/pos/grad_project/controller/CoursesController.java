package com.pos.grad_project.controller;

import com.pos.grad_project.AppConstants;
import com.pos.grad_project.model.dto.CoursesReqDTO;
import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.service.CoursesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = AppConstants.frontEnd)
@RequestMapping("/courses")
public class CoursesController {
    private final CoursesService coursesService;
    @GetMapping("/all-courses")
    public ResponseEntity<?> allCourses(@RequestParam Grade grade,@RequestParam String category,@RequestParam int page,@RequestParam int size) {
        CoursesReqDTO coursesReqDTO = new CoursesReqDTO(grade,category);
        return this.coursesService.allCourses(coursesReqDTO,page,size);
    }
    @GetMapping("/filter-courses")
    public ResponseEntity<?> findCourses(@RequestParam Grade grade,@RequestParam(defaultValue = "0") double minPrice,@RequestParam(defaultValue = "50000") double maxPrice,@RequestParam(defaultValue = "5") float rate,@RequestParam String name,@RequestParam String teacher,@RequestParam String category,@RequestParam int page,@RequestParam int size) {
        return this.coursesService.findCoursesByFilters(grade,minPrice,maxPrice,rate,name,teacher,category,page,size);
    }
    @GetMapping("/course-content/{id}")
    public ResponseEntity<?> courseContent(@PathVariable Long id){
        return this.coursesService.courseContent(id);
    }
    @GetMapping("/course-info/{id}")
    public ResponseEntity<?> courseInfo(@PathVariable Long id){
        return this.coursesService.courseInfo(id);
    }
    @GetMapping("/teacher-of-course-info/{id}")
    public ResponseEntity<?> teacherOfCourseInfo(@PathVariable Long id){
        return this.coursesService.teacherOfCourseInfo(id);
    }
    @GetMapping("/course-reviews/{id}")
    public ResponseEntity<?> courseReviews(@PathVariable Long id){
        return this.coursesService.courseReview(id);
    }
    @GetMapping("/related-courses/{id}")
    public ResponseEntity<?> relatedCourses(@PathVariable Long id){
        return this.coursesService.relatedCourses(id);
    }
    @GetMapping("/search/{name}/{page}/{size}")
    public ResponseEntity<?> search(@PathVariable String name,@PathVariable int page,@PathVariable int size) {
        return this.coursesService.searchCourse(name,page,size);
    }

}
