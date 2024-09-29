package com.kerolos.newspaper.controller;

import com.kerolos.newspaper.data.dto.NewsRequest;
import com.kerolos.newspaper.data.dto.NewsResponse;
import com.kerolos.newspaper.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(NewsController.PATH)
public class NewsController {

    public static final String PATH = "v1/news";

    private final NewsService newsService;


    @Operation(summary = "Create news", description = "Allows a Content Writer or Admin to create a news item. The news is created with pending status and requires Admin approval.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyRole('CONTENT_WRITER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<NewsResponse> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.createNews(newsRequest));
    }

    @Operation(summary = "Approve news", description = "Allows an admin to approve a news item. Only admins have permission to perform this action.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News approved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid news ID or approval error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{newsId}/approve")
    public ResponseEntity<NewsResponse> approveNews(@PathVariable Long newsId) {
        return ResponseEntity.ok(newsService.approveNews(newsId));
    }

    @Operation(summary = "Request to delete news", description = "Allows a Content Writer or Admin to request the deletion of a news item. If the news is pending, the content writer can delete it, but if it is already approved, an admin must approve the deletion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "News deletion request submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid news ID or deletion error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions"),
            @ApiResponse(responseCode = "404", description = "News not found")
    })
    @PreAuthorize("hasAnyRole('CONTENT_WRITER', 'ADMIN')")
    @DeleteMapping("/{newsId}")
    public ResponseEntity<Void> requestDeleteNews(@PathVariable Long newsId) {
        newsService.requestDeleteNews(newsId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all approved news", description = "Retrieves a list of all approved news items. Available to users, content writers, and admins.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of approved news",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyRole('USER', 'CONTENT_WRITER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<NewsResponse>> getApprovedNews() {
        return ResponseEntity.ok(newsService.getApprovedNews());
    }

    @Operation(summary = "Get a specific approved news item", description = "Retrieves a specific news item by its ID if it has been approved. Available to users, content writers, and admins.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the approved news item",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))),
            @ApiResponse(responseCode = "404", description = "News item not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyRole('USER', 'CONTENT_WRITER', 'ADMIN')")
    @GetMapping("/{newsId}")
    public ResponseEntity<NewsResponse> getOneApprovedNews(@Parameter(description = "ID of the news to retrieve", required = true)
                                                           @PathVariable Long newsId) {
        return newsService.getApprovedNews(newsId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update news", description = "Allows a Content Writer or Admin to update an existing news item. Content Writers can update pending news, while Admins can update any news.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewsResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or update error",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden, user does not have sufficient permissions",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PreAuthorize("hasAnyRole('CONTENT_WRITER', 'ADMIN')")
    @PutMapping("/{newsId}")
    public ResponseEntity<NewsResponse> updateNews(@Parameter(description = "ID of the news to update", required = true)
                                                   @PathVariable Long newsId,
                                                   @Parameter(description = "Updated news details", required = true)
                                                   @RequestBody NewsRequest updateNewsRequest) {
        return ResponseEntity.ok(newsService.updateNews(newsId, updateNewsRequest));
    }

}

