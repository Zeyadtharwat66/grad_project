package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CartItemEntity;
import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.StudentMyCourseItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyCartItemRepo extends JpaRepository<CartItemEntity,Long> {
   Page<CartItemEntity> findAllByStudentId(long studentId, Pageable pagebale);
   Boolean existsByCourseAndStudent(CourseEntity course,StudentEntity student);
   Boolean existsByStudent(StudentEntity student);
   @Query("SELECT SUM(c.course.price) FROM CartItemEntity c WHERE c.student.id = :studentId")
   Double getTotalCartPrice(Long studentId);
   CartItemEntity findByCourse(CourseEntity course);
   List<CartItemEntity> findAllByStudent(StudentEntity student);
   void deleteAllByStudent(StudentEntity student);
}
