package com.pos.grad_project.controller;

import com.pos.grad_project.model.dto.LoginDTO;
import com.pos.grad_project.model.dto.RegisterDTO;
import com.pos.grad_project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final StudentService studentService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        return studentService.login(loginDTO);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO registerDTO) {
        return studentService.register(registerDTO);
    }
}
