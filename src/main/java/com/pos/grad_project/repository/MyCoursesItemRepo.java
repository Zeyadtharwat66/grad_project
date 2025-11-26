package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.StudentMyCourseItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyCoursesItemRepo extends JpaRepository<StudentMyCourseItemEntity,Long> {
    Page<StudentMyCourseItemEntity> findAllByStudentId(long studentId, Pageable peagable);
    Boolean existsByCourse(CourseEntity course);
    StudentMyCourseItemEntity findByCourse(CourseEntity course);
}
