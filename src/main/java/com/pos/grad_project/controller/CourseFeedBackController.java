package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import com.pos.grad_project.service.CourseFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/courseFeedBack")
public class CourseFeedBackController {
    private final CourseFeedbackService courseFeedbackService;
    @PostMapping("/add")
    public ResponseEntity<?> addFeedBack(@RequestBody CourseFeedbackReqDTO courseFeedbackReqDTO){
        return this.courseFeedbackService.addFeedback(courseFeedbackReqDTO);
    }

    @DeleteMapping("/delete/{courseFeedbackId}")
    public ResponseEntity<?> deleteFeedBack(@PathVariable(name = "courseFeedbackId") Long courseFeedbackId){
        return this.courseFeedbackService.deleteFeedback(courseFeedbackId);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateFeedBack(@RequestBody CourseFeedbackUpdateReqDTO courseFeedbackupdateReqDTO){
        return this.courseFeedbackService.updateFeedback(courseFeedbackupdateReqDTO);
    }
}
