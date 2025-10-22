package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pos.grad_project.model.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments_submission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at is null")
public class AssignmentSubmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_assignmet_id")
    private Long id;
    @Column(name = "submission_url")
    private String submissionUrl;
    private Double grade;
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;
    @CreationTimestamp
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private TeacherAssignmentEntity assignment;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private VideoEntity video;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherEntity teacher;
}
