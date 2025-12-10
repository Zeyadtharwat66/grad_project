package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class SectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="section_id")
    private Long id;
    private String title;
    @Column(name="total_lessons")
    private Integer totalLessons;
    private Duration duration;
    @Column(name="order_index")
    private Integer orderIndex;
    @Column(name="is_completed")
    private Boolean isCompleted;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @JsonManagedReference
    @OneToMany(mappedBy = "section")
    private List<QuizFromTeacherEntity> quizFromTeacher;

    @JsonManagedReference
    @OneToOne(mappedBy = "section")
    private MaterialEntity material;

    @JsonManagedReference
    @OneToMany(mappedBy = "section")
    private List<VideoEntity> video;
}
