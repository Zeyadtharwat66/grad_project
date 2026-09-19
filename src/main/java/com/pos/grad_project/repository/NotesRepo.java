package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.NotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepo extends JpaRepository<NotesEntity, Long> {
    boolean existsByNoteAndStudentIdAndVideosId(String note, Long studentId, Long videoId);
    List<NotesEntity> findByVideosIdAndStudentId(long videoId, long studentId);
}
