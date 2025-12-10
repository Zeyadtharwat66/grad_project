package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.SectionEntity;
import com.pos.grad_project.model.entity.StudentVideoProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentVideoProgressRepo extends JpaRepository<StudentVideoProgressEntity, Long> {
    StudentVideoProgressEntity findByVideoIdAndStudentId(long videoId, long studentId);
    int countByStudentIdAndCourseId(long studentId,long courseId);
    int countByStudentIdAndCompletedAndCourseId(long studentId, boolean isCompleted,Long courseId);
}
