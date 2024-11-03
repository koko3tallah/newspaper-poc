package com.kerolos.newspaper.service.impl;

import com.kerolos.newspaper.data.dto.NewsRequest;
import com.kerolos.newspaper.data.dto.NewsResponse;
import com.kerolos.newspaper.data.entity.News;
import com.kerolos.newspaper.data.entity.User;
import com.kerolos.newspaper.data.enums.NewsStatus;
import com.kerolos.newspaper.mapper.NewsMapper;
import com.kerolos.newspaper.repository.NewsRepository;
import com.kerolos.newspaper.security.CustomUserDetailsService;
import com.kerolos.newspaper.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional
    public NewsResponse createNews(NewsRequest newsRequest) {
        User contentWriter = userDetailsService.getLoggedinUser();

        News news = new News();
        news.setTitle(newsRequest.getTitle());
        news.setTitleAr(newsRequest.getTitleAr());
        news.setDescription(newsRequest.getDescription());
        news.setDescriptionAr(newsRequest.getDescriptionAr());
        news.setPublishDate(newsRequest.getPublishDate());
        news.setCreatedBy(contentWriter);
        news.setContentWriter(contentWriter);
        news.setImageUrl(newsRequest.getImageUrl());

        if (userDetailsService.isCurrentUserAdmin()) {
            news.setStatus(NewsStatus.APPROVED);
            news.setApprovedAt(LocalDateTime.now());
            news.setApprovedBy(contentWriter);
        } else {
            news.setStatus(NewsStatus.PENDING);
        }

        News savedNews = newsRepository.save(news);
        return newsMapper.mapToNewsDTO(savedNews);
    }

    @Override
    @Transactional
    public NewsResponse approveNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        if (news.getStatus() == NewsStatus.APPROVED || news.getStatus() == NewsStatus.APPROVED_DELETE) {
            throw new RuntimeException("News already approved or deleted");
        }

        User admin = userDetailsService.getLoggedinUser();

        if (news.getStatus() == NewsStatus.PENDING) {
            news.setStatus(NewsStatus.APPROVED);
        } else if (news.getStatus() == NewsStatus.PENDING_DELETE) {
            news.setStatus(NewsStatus.APPROVED_DELETE);
            news.setDeleted(true);
        }
        news.setApprovedBy(admin);
        news.setApprovedAt(LocalDateTime.now());

        News approvedNews = newsRepository.save(news);
        return newsMapper.mapToNewsDTO(approvedNews);
    }

    @Override
    @Transactional
    public void requestDeleteNews(Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        if (news.getStatus() == NewsStatus.PENDING_DELETE || news.getStatus() == NewsStatus.APPROVED_DELETE) {
            throw new RuntimeException("News already deleted");
        }

        if (news.getStatus() == NewsStatus.PENDING || userDetailsService.isCurrentUserAdmin()) {
            news.setDeleted(true);
            news.setStatus(NewsStatus.APPROVED_DELETE);
        } else {
            news.setStatus(NewsStatus.PENDING_DELETE);
        }

        news.setDeletedAt(LocalDateTime.now());
        newsRepository.save(news);
    }

    @Override
    public List<NewsResponse> getApprovedNews() {
        return newsRepository.findByStatusOrderByPublishDateDesc(NewsStatus.APPROVED)
                .stream()
                .map(newsMapper::mapToNewsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NewsResponse> getApprovedNews(Long newsId) {
        return newsRepository.findByIdAndStatus(newsId, NewsStatus.APPROVED)
                .map(newsMapper::mapToNewsDTO);
    }

    @Override
    @Transactional
    public NewsResponse updateNews(Long newsId, NewsRequest updateNewsRequest) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));

        boolean isAdmin = userDetailsService.isCurrentUserAdmin();

        if (!isAdmin && news.getStatus() != NewsStatus.PENDING) {
            throw new RuntimeException("Only admins can update approved news.");
        }

        news.setTitle(updateNewsRequest.getTitle());
        news.setTitleAr(updateNewsRequest.getTitleAr());
        news.setDescription(updateNewsRequest.getDescription());
        news.setDescriptionAr(updateNewsRequest.getDescriptionAr());
        news.setPublishDate(updateNewsRequest.getPublishDate());
        news.setImageUrl(updateNewsRequest.getImageUrl());

        News updatedNews = newsRepository.save(news);

        return newsMapper.mapToNewsDTO(updatedNews);
    }

}
