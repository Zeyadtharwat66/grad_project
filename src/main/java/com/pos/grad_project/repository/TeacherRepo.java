package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.TeacherEntity;
import com.pos.grad_project.model.enums.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepo extends JpaRepository<TeacherEntity,Long> {
    List<TeacherEntity> findTop2ByOrderByNumberOfStudentsDesc();
    long countByDeletedAtIsNull();
    TeacherEntity findByUsername(String name);
    List<TeacherEntity> findBySpecialization(String specialization);
}
