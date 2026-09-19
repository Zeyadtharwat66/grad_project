package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackResDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.repository.CourseFeedbackRepo;
import com.pos.grad_project.repository.CourseRepo;
import com.pos.grad_project.repository.MyCoursesItemRepo;
import com.pos.grad_project.repository.StudentRepo;
import com.pos.grad_project.service.CourseFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CourseFeedbackServiceImp implements CourseFeedbackService {
    private final CourseFeedbackRepo courseFeedbackRepo;
    private final CourseRepo courseRepo;
    private final StudentRepo studentRepo;
    private final MyCoursesItemRepo myCoursesItemRepo;

    @Override
    public ResponseEntity<?> addFeedback(CourseFeedbackReqDTO request) {
        StudentEntity student = getAuthenticatedStudent();
        CourseEntity course = courseRepo.findById(request.courseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (!myCoursesItemRepo.existsByCourseAndStudent(course, student)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must enroll in the course before reviewing it");
        }
        if (request.rating() < 0 || request.rating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
        }
        if (request.comment() == null || request.comment().isBlank()) {
            return ResponseEntity.badRequest().body("Comment is required");
        }

        CourseFeedbackEntity feedback = CourseFeedbackEntity.builder()
                .course(course)
                .student(student)
                .comment(request.comment().trim())
                .rating(request.rating())
                .build();

        courseFeedbackRepo.save(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @Override
    public ResponseEntity<?> updateFeedback(CourseFeedbackUpdateReqDTO request) {
        StudentEntity student = getAuthenticatedStudent();
        CourseFeedbackEntity feedback = courseFeedbackRepo.findById(request.feedbackID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

        if (!feedback.getStudent().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this feedback");
        }
        if (request.rating() < 0 || request.rating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 0 and 5");
        }
        if (request.comment() == null || request.comment().isBlank()) {
            return ResponseEntity.badRequest().body("Comment is required");
        }

        feedback.setComment(request.comment().trim());
        feedback.setRating(request.rating());
        courseFeedbackRepo.save(feedback);
        return ResponseEntity.ok(feedback);
    }

    @Override
    public ResponseEntity<?> deleteFeedback(Long id) {
        StudentEntity student = getAuthenticatedStudent();
        CourseFeedbackEntity feedback = courseFeedbackRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

        if (!feedback.getStudent().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this feedback");
        }

        feedback.setDeletedAt(java.time.LocalDateTime.now());
        courseFeedbackRepo.save(feedback);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

    private StudentEntity getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return studentRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Student not found"));
    }
}
