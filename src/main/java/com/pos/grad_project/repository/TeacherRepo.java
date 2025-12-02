package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepo extends JpaRepository<TeacherEntity,Long> {
    List<TeacherEntity> findTop2ByOrderByNumberOfStudentsDesc();
    long countByDeletedAtIsNull();
    TeacherEntity findByUsername(String name);
    public TeacherEntity findById(long id);
    List<TeacherEntity> findBySpecialization(String specialization);
}
