package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CartEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<CartEntity,Long> {
    public CartEntity findByStudent(StudentEntity student);
}
