package com.pos.grad_project.repository;

import com.pos.grad_project.model.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideosRepo extends JpaRepository<VideoEntity,Long> {
}
