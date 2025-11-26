package com.pos.grad_project.service.Imp;

import com.pos.grad_project.model.entity.TeacherEntity;
import com.pos.grad_project.model.enums.Grade;
import com.pos.grad_project.repository.TeacherRepo;
import com.pos.grad_project.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImp implements TeacherService {
    private final TeacherRepo teacherRepo;
    @Override
    public ResponseEntity<?> filterResult(String specialization) {
        List<TeacherEntity> teachers = teacherRepo.findBySpecialization(specialization);
        if(teachers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No teachers found");
        }
        List<String> teacherNames=teachers.stream()
                .map(t->t.getUsername()).collect(Collectors.toList());
        return ResponseEntity.ok(teacherNames);
    }
}
