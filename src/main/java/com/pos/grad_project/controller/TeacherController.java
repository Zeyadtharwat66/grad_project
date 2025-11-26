package com.pos.grad_project.controller;

import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    @GetMapping("/filters/{specialization}")
    public ResponseEntity<?> filterResult(@PathVariable String specialization){
        return this.teacherService.filterResult(specialization);
    }
}
