package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CategoryEntity;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.TeacherEntity;
import com.pos.grad_project.model.enums.Grade;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CourseRepo extends JpaRepository<CourseEntity,Long> {
    Page<CourseEntity> findByCategoryAndGrade(CategoryEntity category, Grade grade,Pageable pageable);
    List<CourseEntity> findByCategoryAndGrade(CategoryEntity category, Grade grade);
    List<CourseEntity> findTop6ByOrderByNumberOfStudentsDesc();
    long countByDeletedAtIsNull();
    Page<CourseEntity> findAllByName(String name,Pageable pageable);
    Page<CourseEntity> findByPriceBetweenAndRatingGreaterThanEqualAndCategoryAndGradeAndTeacherAndName(double minPrice,double maxPrice, float rate, CategoryEntity category, Grade grade, TeacherEntity teacher, String name,Pageable pageable);
}
