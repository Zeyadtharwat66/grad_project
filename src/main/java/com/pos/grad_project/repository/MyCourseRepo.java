package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.StudentMyCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyCourseRepo extends JpaRepository<StudentMyCourseEntity,Long> {
    StudentMyCourseEntity findByStudent(StudentEntity student);
}
