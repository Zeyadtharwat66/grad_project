package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.enums.Grade;
import org.springframework.http.ResponseEntity;

public interface TeacherService {
    public ResponseEntity<?> filterResult(String specialization);
}
