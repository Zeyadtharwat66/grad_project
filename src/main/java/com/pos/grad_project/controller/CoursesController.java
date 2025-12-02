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
    @GetMapping("/filter-courses")
    public ResponseEntity<?> findCourses(@RequestParam(required = false) String grade,@RequestParam(required = false) Double priceMin,@RequestParam(required = false) Double priceMax,@RequestParam(required = false) Float rate,@RequestParam(required = false) String name,@RequestParam(required = false) Long teacher,@RequestParam(required = false) String category,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "4") int size) {
        return this.coursesService.findCoursesByFilters(grade,priceMin,priceMax,rate,name,teacher,category,page,size);
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
    @GetMapping("/get-material/{sectionId}")
    public ResponseEntity<?> getMaterial(@PathVariable Long sectionId){
        return this.coursesService.showMaterial(sectionId);
    }
    @PostMapping("/add-note/{note}/{sectionId}/{studentId}")
    public ResponseEntity<?> addNote(@PathVariable String note,@PathVariable Long sectionId,@PathVariable Long studentId){
        return this.coursesService.addNote(note,sectionId,studentId);
    }
    @PutMapping("/update-note/{note}/{noteId}/{studentId}")
    public ResponseEntity<?> update(@PathVariable String note,@PathVariable Long noteId,@PathVariable Long studentId){
        return this.coursesService.updateNote(note,noteId,studentId);
    }
    @DeleteMapping("/delete-note/{noteId}/{studentId}")
    public ResponseEntity<?> delete(@PathVariable Long noteId,@PathVariable Long studentId){
        return this.coursesService.deleteNote(noteId,studentId);
    }
    @GetMapping("/get-progress/{studentId}/{courseId}")
    public ResponseEntity<?> getProgress(@PathVariable Long studentId,@PathVariable Long courseId){
        return this.coursesService.getProgress(studentId,courseId);
    }
}
