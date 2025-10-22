package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pos.grad_project.model.enums.MaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_submission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class QuizSubmitedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quiz_submission_id")
    private Long id;
    @Column(name="submitted_at")
    @CreationTimestamp
    private LocalDateTime submittedAt;
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    @Column(name = "file_url")
    private String fileUrl;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="student_id", nullable = false)
    private StudentEntity student;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "quiz_teacher_id", nullable = false)
    private QuizFromTeacherEntity quizFromTeachers;

}
