package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.StudentEntity;
import com.pos.grad_project.model.entity.StudentMyCourseItemEntity;
import com.pos.grad_project.model.entity.WishlistItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyWishListItemsRepo extends JpaRepository<WishlistItemEntity,Long> {
    Page<WishlistItemEntity> findAllByStudentId(long studentId, Pageable pageable);
    Boolean existsByCourseAndStudent(CourseEntity course, StudentEntity student);
    WishlistItemEntity findByCourse(CourseEntity course);
    Boolean existsByStudent(StudentEntity student);
    @Query("SELECT SUM(c.course.price) FROM WishlistItemEntity c WHERE c.student.id = :studentId")
    Double getTotalCartPrice(Long studentId);

    List<WishlistItemEntity> findAllByStudent(StudentEntity student);
}
