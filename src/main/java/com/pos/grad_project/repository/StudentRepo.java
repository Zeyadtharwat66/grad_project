package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    public Optional<StudentEntity> findByUsername(String username);
    public StudentEntity findById(long id);
    long countByDeletedAtIsNull();
}
