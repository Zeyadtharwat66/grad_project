package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import com.pos.grad_project.service.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    private final StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return studentService.login(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return studentService.register(registerDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        return studentService.getCurrentUser();
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return studentService.logout();
    }

}