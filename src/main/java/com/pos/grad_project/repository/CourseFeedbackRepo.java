package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseFeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseFeedbackRepo extends JpaRepository<CourseFeedbackEntity,Long> {
    @Query(value = "SELECT * FROM courses_feedback ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
    List<CourseFeedbackEntity> findRandom6();
    long findCourseIdById(Long id);
    long findStudentIdById(Long id);
    long countByCourseId(Long courseId);
}
