package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.CourseFeedbackReqDTO;
import com.pos.grad_project.model.dto.CourseFeedbackUpdateReqDTO;
import org.springframework.http.ResponseEntity;

public interface CourseFeedbackService {
    public ResponseEntity<?> addFeedback(CourseFeedbackReqDTO courseFeedbackReqDTO);
    public ResponseEntity<?> updateFeedback(CourseFeedbackUpdateReqDTO courseFeedbackUpdateReqDTO);
    public ResponseEntity<?> deleteFeedback(Long id);
}
