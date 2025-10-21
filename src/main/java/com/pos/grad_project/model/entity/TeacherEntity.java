package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.Gender;
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
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_id")
    private Long id;
    @NotNull()
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_*])(?=\\S+$).{8,}$",message = "Strong password")
    private String password;
    @NotNull()
    private String username;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    @Email
    @NotNull()
    @Column(unique = true)
    private String email;
    @NotNull()
    @Pattern(regexp = "^(01[0-2,5]{1}[0-9]{8})$", message = "Invalid Egyptian phone number")
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String specialization;
    private String IBAN;
    @Column(name = "number_of_students")
    private Integer numberOfStudents;
    @Column(name = "number_of_courses")
    private Integer numberOfCourses;
    @Column(name="birth_date",columnDefinition = "DATE")
    private LocalDate birthDate;
    private Double rate;
    private String bio;
    @Lob //long object
    @Column(columnDefinition = "LONGBLOB",name="validate_teacher")
    private byte[] validateTeacher;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<CourseEntity> courses;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<MaterialEntity> materials;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<QuizFromTeacherEntity> quizFromTeachers;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<CouponEntity> coupons;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<AssignmentEntity> assignments;

    @JsonManagedReference
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL)
    private List<StudentNotificationEntity> studentNotifications;


}
