package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import com.pos.grad_project.service.CourseFeedbackService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/courseFeedBack")
public class CourseFeedBackController {
    private final CourseFeedbackService courseFeedbackService;

    @PostMapping("/add")
    public ResponseEntity<?> addFeedBack(@Valid @RequestBody CourseFeedbackReqDTO request) {
        return courseFeedbackService.addFeedback(request);
    }

    @DeleteMapping("/delete/{courseFeedbackId}")
    public ResponseEntity<?> deleteFeedBack(@PathVariable Long courseFeedbackId) {
        return courseFeedbackService.deleteFeedback(courseFeedbackId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateFeedBack(@Valid @RequestBody CourseFeedbackUpdateReqDTO request) {
        return courseFeedbackService.updateFeedback(request);
    }
}
