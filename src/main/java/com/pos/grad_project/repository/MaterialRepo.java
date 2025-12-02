package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.MaterialEntity;
import com.pos.grad_project.model.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepo extends JpaRepository<MaterialEntity,Long> {
    MaterialEntity findBySection(SectionEntity section);
}
