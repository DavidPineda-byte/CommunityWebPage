package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentItemService {

    List<ContentItem> getAllContentItems();
    List<ContentItem> findContentByAuthor(String author);
    ContentItem findContentByTitle(String title);
    List<ContentItem> findContentByGenre(Genre genre);
    ContentItem findContentById(Long id);
    List<ContentItem> findContentByDate(LocalDateTime date);
    List<ContentItem> findFeaturedContent();
    <T extends ContentItem> List<T> findFeaturedContentByType(Class<T>  type);
    List<ContentItem> findFeaturedContentByCategory(Category category);

    // ── Content approval methods ──
    // Retrieves all content items matching a given approval status
    List<ContentItem> findContentByStatus(ContentStatus status);

    // Retrieves only APPROVED content (convenience method for public-facing pages)
    List<ContentItem> findApprovedContent();
}
