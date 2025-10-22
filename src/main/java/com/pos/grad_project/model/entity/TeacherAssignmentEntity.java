package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.MaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "teacher_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class TeacherAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="assignment_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private MaterialType type;
    private String title;
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "full_mark")
    private Double fullMark;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "assignment",cascade = CascadeType.ALL)
    private List<AssignmentSubmissionEntity> studentAssignments;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name="video_id",nullable = false)
    private VideoEntity video;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="course_id",nullable = false)
    private CourseEntity course;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id",nullable = false)
    private TeacherEntity teacher;
}
