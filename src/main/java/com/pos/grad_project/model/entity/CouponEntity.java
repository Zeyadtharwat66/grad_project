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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    private Long id;
    @Column(unique = true,name = "coupon_code")
    private String couponCode;
    @Column(name = "discount_rate")
    private Float discountRate;
    @Column(name = "discount_amount")
    private Double discountAmount;
    @Column(name = "max_usage")
    private Integer maxUsage;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "used_count")
    private Integer usedCount;
    @Column(name = "is_active")
    private Boolean isActive;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "coupon",cascade = CascadeType.ALL)
    private List<Course_CouponEntity> courseCoupons;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;


}
