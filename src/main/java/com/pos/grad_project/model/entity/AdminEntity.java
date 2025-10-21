package com.pos.grad_project.model.entity;
import com.pos.grad_project.model.enums.MaterialType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class AdminEntity {//not completed
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_id")
    private Long id;
    @Column(unique = true)
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_*])(?=\\S+$).{8,}$",message = "Strong password")
    private String password;
    @Email
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    @Pattern(regexp = "^(01[0-2,5]{1}[0-9]{8})$", message = "Invalid Egyptian phone number")
    private String phone;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;
}
