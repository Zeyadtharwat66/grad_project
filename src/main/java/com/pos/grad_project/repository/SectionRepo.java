package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SectionRepo extends JpaRepository<SectionEntity, Long> {
    SectionEntity findById(long id);
}
