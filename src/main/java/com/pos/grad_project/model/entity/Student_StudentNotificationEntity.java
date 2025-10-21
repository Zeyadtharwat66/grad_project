package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_student-notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student_StudentNotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isRead = false;
    private LocalDateTime sentAt = LocalDateTime.now();
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "notification_id")
    private StudentNotificationEntity notification;

}
