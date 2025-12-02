package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class NotesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="note_id")
    private Long id;
    private String note;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionEntity section;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;
}
