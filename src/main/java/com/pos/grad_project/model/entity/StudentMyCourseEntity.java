package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
@Entity
@Table(name = "student_my_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class StudentMyCourseEntity {//many to many w de faktha
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_my_courses_id")
    private Long id;
    private Double progress;
    private Double grade;
    @Column(name = "is_completed")
    private Boolean isCompleted;
    @Column(name = "courses_count")
    private Integer coursesCount;
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
    @JoinColumn(name="course_id")
    private CourseEntity course;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="student_id")
    private StudentEntity student;
}
