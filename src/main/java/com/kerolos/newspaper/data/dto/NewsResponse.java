package com.kerolos.newspaper.data.dto;

import com.kerolos.newspaper.data.enums.NewsStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewsResponse {

    private Long id;
    private String title;
    private String titleAr;
    private String description;
    private String descriptionAr;
    private LocalDate publishDate;
    private String imageUrl;
    private NewsStatus status;

    private String createdByFullName;
    private String approvedByFullName;
    private LocalDateTime approvedAt;

}
