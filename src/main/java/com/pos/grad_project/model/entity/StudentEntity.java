package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.Gender;
import com.pos.grad_project.model.enums.Grade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long id;
    @NotNull()
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_*])(?=\\S+$).{8,}$",message = "Strong password")
    private String password;
    @NotNull()
    private String username;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    private String role;
    @Email
    @NotNull()
    @Column(unique = true)
    private String email;
    @NotNull()
    @Pattern(regexp = "^(01[0-2,5]{1}[0-9]{8})$", message = "Invalid Egyptian phone number")
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Grade grade;
    @Column(name="birth_date",columnDefinition = "DATE")
    private LocalDate birthDate;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<WishListEntity> wishlists;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<CartEntity> cart;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<StudentMyCourseEntity> myCourses;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<CourseFeedbackEntity> feedbacks;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<CourseProgressEntity> progress;
    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Student_StudentNotificationEntity> studentNotifications;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<AssignmentSubmissionEntity> studentAssignments;
    @JsonManagedReference
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<QuizSubmitedEntity> submissions;
}
