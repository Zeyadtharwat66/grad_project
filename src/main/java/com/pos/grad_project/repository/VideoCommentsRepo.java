package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.CourseEntity;
import com.pos.grad_project.model.entity.VideoCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VideoCommentsRepo extends JpaRepository<VideoCommentEntity,Long>{
    Boolean existsByComment(String comment);
    List<VideoCommentEntity> findByVideoId(Long videoId);
}
