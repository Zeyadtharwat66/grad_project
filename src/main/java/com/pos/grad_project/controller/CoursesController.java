package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.NoteReqDTO;
import com.pos.grad_project.model.dto.UpdateNoteRequest;
import com.pos.grad_project.service.CoursesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CoursesController {
    private final CoursesService coursesService;

    @GetMapping("/filter-courses")
    public ResponseEntity<?> findCourses(@RequestParam(required = false) String grade,
                                         @RequestParam(required = false) Double priceMin,
                                         @RequestParam(required = false) Double priceMax,
                                         @RequestParam(required = false) Float rate,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) Long teacher,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(defaultValue = "0") @Min(0) int page,
                                         @RequestParam(defaultValue = "4") @Min(1) @Max(100) int size) {
        return coursesService.findCoursesByFilters(grade, priceMin, priceMax, rate, name, teacher, category, page, size);
    }

    @GetMapping("/course-content/{id}")
    public ResponseEntity<?> courseContent(@PathVariable Long id) {
        return coursesService.courseContent(id);
    }

    @GetMapping("/teacher-of-course-info/{id}")
    public ResponseEntity<?> teacherOfCourseInfo(@PathVariable Long id) {
        return coursesService.teacherOfCourseInfo(id);
    }

    @GetMapping("/course-reviews/{id}")
    public ResponseEntity<?> courseReviews(@PathVariable Long id) {
        return coursesService.getCourseReviews(id);
    }

    @GetMapping("/related-courses/{id}")
    public ResponseEntity<?> relatedCourses(@PathVariable Long id) {
        return coursesService.relatedCourses(id);
    }

    @PostMapping("/add-feedback")
    public ResponseEntity<?> addCourseReviews(@RequestParam Long id,
                                               @RequestParam String comment,
                                               @RequestParam(defaultValue = "0.0") float rate) {
        return coursesService.addCourseReviews(id, comment, rate);
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<?> video(@PathVariable Long id) {
        return coursesService.showVideosPage(id);
    }

    @GetMapping("/search/{name}/{page}/{size}")
    public ResponseEntity<?> search(@PathVariable String name, @PathVariable @Min(0) int page, @PathVariable @Min(1) @Max(100) int size) {
        return coursesService.searchCourse(name, page, size);
    }

    @GetMapping("/get-material/{sectionId}")
    public ResponseEntity<?> getMaterial(@PathVariable Long sectionId) {
        return coursesService.showMaterial(sectionId);
    }

    @PostMapping("/add-note/{id}")
    public ResponseEntity<?> addNote(@Valid @RequestBody NoteReqDTO noteReqDTO, @PathVariable long id) {
        return coursesService.addNote(noteReqDTO);
    }

    @PutMapping("/update-note/{noteId}")
    public ResponseEntity<?> update(@PathVariable Long noteId,
                                    @Valid @RequestBody UpdateNoteRequest request) {
        return coursesService.updateNote(request, noteId);
    }

    @GetMapping("/get-note/{videoId}")
    public ResponseEntity<?> getNote(@PathVariable Long videoId) {
        return coursesService.getNote(videoId);
    }

    @DeleteMapping("/delete-note/{noteId}")
    public ResponseEntity<?> delete(@PathVariable Long noteId) {
        return coursesService.deleteNote(noteId);
    }

    @GetMapping("/get-progress/{courseId}")
    public ResponseEntity<?> getProgress(@PathVariable Long courseId) {
        return coursesService.getProgress(courseId);
    }

    @PostMapping("/complete-video/{id}")
    public ResponseEntity<?> completeVideo(@PathVariable Long id) {
        return coursesService.completeVideo(id);
    }
}
