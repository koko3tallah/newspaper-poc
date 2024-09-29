package com.kerolos.newspaper.data.entity;

import com.kerolos.newspaper.data.enums.NewsStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class News extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String titleAr;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descriptionAr;

    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    @NotNull
    @ManyToOne
    private User contentWriter;

    private Boolean deleted = false;

    private LocalDateTime deletedAt;

}