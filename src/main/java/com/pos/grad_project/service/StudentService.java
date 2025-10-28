package com.pos.grad_project.service;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import org.springframework.http.ResponseEntity;

public interface StudentService {
    public ResponseEntity<?> login(LoginDTO loginDTO);
    public ResponseEntity<?> register(RegisterDTO registerDTO);
}
