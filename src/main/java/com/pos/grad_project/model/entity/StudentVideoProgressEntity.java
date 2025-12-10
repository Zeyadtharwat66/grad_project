package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
@Entity
@Table(name = "student_video_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentVideoProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private VideoEntity video;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
    @Column(name = "is_completed", nullable = false)
    private boolean completed;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
