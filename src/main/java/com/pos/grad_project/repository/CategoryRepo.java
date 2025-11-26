package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByName(String name);
}
