package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.NotificationType;
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
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class StudentNotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notification_id")
    private Long id;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Column(name = "is_read")
    private Boolean isRead;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private CourseEntity course;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = true)
    private TeacherEntity teacher;

    @JsonManagedReference
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<Student_StudentNotificationEntity> studentNotifications;
}
