package com.kerolos.newspaper.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class NewsRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Title in Arabic is required")
    private String titleAr;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Description in Arabic is required")
    private String descriptionAr;

    @NotNull(message = "Publish date is required")
    private LocalDate publishDate;

    private String imageUrl;

}
