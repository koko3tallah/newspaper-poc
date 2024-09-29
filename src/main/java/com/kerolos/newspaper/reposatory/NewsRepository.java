package com.kerolos.newspaper.reposatory;

import com.kerolos.newspaper.data.entity.News;
import com.kerolos.newspaper.data.enums.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByStatusOrderByPublishDateDesc(NewsStatus status);

    Optional<News> findByIdAndStatus(Long id, NewsStatus status);

    List<News> findByStatusAndDeletedFalse(NewsStatus status);

    List<News> findByPublishDateBeforeAndDeletedFalse(LocalDate date);

}
