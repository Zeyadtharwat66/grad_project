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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_my_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class StudentMyCourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_my_courses_id")
    private Long id;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "studentMyCourse",cascade = CascadeType.ALL)
    private List<StudentMyCourseEntity> studenyMyCourse;

}
