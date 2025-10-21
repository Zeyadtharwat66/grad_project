package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course_CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;
}
