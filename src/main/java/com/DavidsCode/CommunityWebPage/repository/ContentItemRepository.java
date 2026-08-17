package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ContentItemRepository extends JpaRepository<ContentItem, Long> {

    List<ContentItem> findByFeaturedTrue();
    @Query("SELECT c FROM ContentItem c WHERE c.featured = true AND TYPE(c) = :type")
    <T extends ContentItem> List<T> findByFeaturedTrueAndType(@Param("type") Class<T> type);
    List<ContentItem> findByGenre(Genre genre);
    List<ContentItem> findByFeaturedTrueAndCategory(Category category);
    List<ContentItem> findByCategory(Category category);
    List<ContentItem> findByAuthor(String author);
    List<ContentItem> findByAuthorOrderByCreatedAtDesc(String author);
    ContentItem findByTitle(String title);
    List<ContentItem> findByCreatedAt(LocalDateTime date);

    // ── Content approval queries ──
    // Returns all content with a specific approval status (PENDING, APPROVED, REJECTED)
    List<ContentItem> findByStatus(ContentStatus status);

    // Returns content filtered by both status and type (e.g., only APPROVED Blogs)
    @Query("SELECT c FROM ContentItem c WHERE c.status = :status AND TYPE(c) = :type")
    <T extends ContentItem> List<T> findByStatusAndType(@Param("status") ContentStatus status, @Param("type") Class<T> type);
}
