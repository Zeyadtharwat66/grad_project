package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.NotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepo extends JpaRepository<NotesEntity, Integer> {
    Boolean existsByNote(String note);
    NotesEntity findById(long id);
}
