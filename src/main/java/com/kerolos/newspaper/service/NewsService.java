package com.kerolos.newspaper.service;

import com.kerolos.newspaper.data.dto.NewsRequest;
import com.kerolos.newspaper.data.dto.NewsResponse;

import java.util.List;
import java.util.Optional;

/**
 * NewsService provides the contract for managing news-related operations such as creating,
 * approving, deleting, updating, and retrieving news articles. It defines the core operations
 * related to the news workflow.
 */
public interface NewsService {

    /**
     * Creates a new news article based on the provided details.
     *
     * @param newsRequest the {@link NewsRequest} containing the news details such as title, description,
     *                    publish date, and more.
     * @return a {@link NewsResponse} representing the newly created news article.
     */
    NewsResponse createNews(NewsRequest newsRequest);

    /**
     * Approves a news article by its ID, changing its status to approved.
     *
     * @param newsId the ID of the news article to approve.
     * @return a {@link NewsResponse} representing the approved news article.
     */
    NewsResponse approveNews(Long newsId);

    /**
     * Requests the deletion of a news article. Content writers can delete pending news, while
     * approved news items require admin approval for deletion.
     *
     * @param newsId the ID of the news article to be deleted.
     */
    void requestDeleteNews(Long newsId);

    /**
     * Retrieves a list of all approved news articles.
     *
     * @return a list of {@link NewsResponse} objects representing all approved news articles.
     */
    List<NewsResponse> getApprovedNews();

    /**
     * Retrieves a specific approved news article by its ID.
     *
     * @param newsId the ID of the news article to retrieve.
     * @return an {@link Optional} containing the {@link NewsResponse} if found, or an empty
     * {@link Optional} if the news article is not approved or does not exist.
     */
    Optional<NewsResponse> getApprovedNews(Long newsId);

    /**
     * Updates an existing news article with new details.
     *
     * @param newsId            the ID of the news article to update.
     * @param updateNewsRequest the {@link NewsRequest} containing the updated news details.
     * @return a {@link NewsResponse} representing the updated news article.
     */
    NewsResponse updateNews(Long newsId, NewsRequest updateNewsRequest);
}
