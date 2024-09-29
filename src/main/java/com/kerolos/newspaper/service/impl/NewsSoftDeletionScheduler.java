package com.kerolos.newspaper.service.impl;

import com.kerolos.newspaper.data.entity.News;
import com.kerolos.newspaper.data.enums.NewsStatus;
import com.kerolos.newspaper.reposatory.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NewsSoftDeletionScheduler {

    private final NewsRepository newsRepository;

    public NewsSoftDeletionScheduler(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Automatically soft-deletes news that have exceeded their publishing date.
     * Runs at 00:00 (midnight) every day
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void autoSoftDeleteNews() {
        LocalDate currentDate = LocalDate.now();
        log.info("Starting automatic soft deletion process for news before {}", currentDate);

        List<News> newsToDelete = newsRepository.findByPublishDateBeforeAndDeletedFalse(currentDate);

        for (News news : newsToDelete) {
            news.setDeleted(true);
            news.setDeletedAt(LocalDateTime.now());
            news.setStatus(NewsStatus.APPROVED_DELETE);
            newsRepository.save(news);
        }
        log.info("Automatic soft deletion process completed.");
    }
}
