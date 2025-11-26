package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepo extends JpaRepository<WishListEntity,Long> {
    WishListEntity findByStudent(StudentEntity student);
}
