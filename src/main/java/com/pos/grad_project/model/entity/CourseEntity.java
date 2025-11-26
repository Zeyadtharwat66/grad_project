package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.Grade;
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
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="course_id")
    private Long id;
    private String name;
    @Column(name="total_lessons")
    private Integer totalLessons;
    private String description;
    private String language;
    private Double price;
    @Column(name="image_url")
    private String imageUrl;
    private Float rating;//// Info should split this table
    private Duration duration;
    @Enumerated(EnumType.STRING)
    private Grade grade;
    private Boolean paid;
    @Column(name="number_of_students")
    private Integer numberOfStudents; // in enrollment/regrestration table, concurrency issue
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
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherEntity teacher;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<WishlistItemEntity> wishlistItem;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItem;



    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<StudentMyCourseItemEntity> myCourses;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Course_CouponEntity> courseCoupons;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<SectionEntity> sections;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CourseFeedbackEntity> feedbacks;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CourseProgressEntity> progress;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<AssignmentSubmissionEntity> studentassignments;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<TeacherAssignmentEntity> teacherAssignments;

    @JsonManagedReference
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<VideoEntity> videos;

}
