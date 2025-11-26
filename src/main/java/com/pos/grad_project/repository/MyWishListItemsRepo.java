package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.StudentMyCourseItemEntity;
import com.pos.grad_project.model.entity.WishlistItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyWishListItemsRepo extends JpaRepository<WishlistItemEntity,Long> {
    Page<WishlistItemEntity> findAllByStudentId(long studentId, Pageable pageable);
    Boolean existsByCourse(CourseEntity course);
    WishlistItemEntity findByCourse(CourseEntity course);
}
