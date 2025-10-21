package com.pos.grad_project.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pos.grad_project.model.enums.VideoStatus;
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
@Table(name = "videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause= "deleted_at is null")
public class VideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="video_id")
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    private String description;
    private Duration duration;
    private String url;
    @Column(name="order_index")
    private Integer orderIndex;
    @Column(name="number_of_likes")
    private Integer numberOfLikes;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime  deletedAt;
    @JsonManagedReference
    @OneToMany(mappedBy = "video",cascade = CascadeType.ALL)
    private List<StudentAssignmentEntity> studentAssignments;
}
