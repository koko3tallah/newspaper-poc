package com.kerolos.newspaper.mapper;

import com.kerolos.newspaper.data.dto.NewsResponse;
import com.kerolos.newspaper.data.entity.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsResponse mapToNewsDTO(News news) {
        NewsResponse dto = new NewsResponse();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setTitleAr(news.getTitleAr());
        dto.setDescription(news.getDescription());
        dto.setDescriptionAr(news.getDescriptionAr());
        dto.setPublishDate(news.getPublishDate());
        dto.setStatus(news.getStatus());
        dto.setCreatedByFullName(news.getCreatedBy().getFullName());
        dto.setImageUrl(news.getImageUrl());

        if (news.getApprovedBy() != null) {
            dto.setApprovedByFullName(news.getApprovedBy().getFullName());
            dto.setApprovedAt(news.getApprovedAt());
        }
        return dto;
    }
}
