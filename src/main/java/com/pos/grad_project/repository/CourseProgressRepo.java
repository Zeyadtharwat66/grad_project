package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.CourseProgressEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseProgressRepo extends JpaRepository<CourseProgressEntity,Long> {
    CourseProgressEntity findByCourseAndStudent(CourseEntity course, StudentEntity student);
}
