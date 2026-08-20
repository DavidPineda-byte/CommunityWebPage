package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.repository.ContentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentItemServiceImpl implements ContentItemService {

    @Autowired
    ContentItemRepository contentItemRepository;


    @Override
    public List<ContentItem> getAllContentItems() {
        return contentItemRepository.findAll();
    }

    @Override
    public List<ContentItem> findContentByAuthor(String author) {
        return contentItemRepository.findByAuthor(author);
    }

    @Override
    public ContentItem findContentByTitle(String title) {
        return contentItemRepository.findByTitle(title);
    }

    @Override
    public List<ContentItem> findContentByGenre(Genre genre) {
        return contentItemRepository.findByGenre(genre);
    }

    @Override
    public ContentItem findContentById(Long id) {
        return contentItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<ContentItem> findContentByDate(LocalDateTime date) {
        return contentItemRepository.findByCreatedAt(date);
    }
    @Override
    public List<ContentItem> findFeaturedContent() {
        return contentItemRepository.findByFeaturedTrue();
    }

    @Override
    public <T extends ContentItem> List<T> findFeaturedContentByType(Class<T> type) {
        return contentItemRepository.findByFeaturedTrueAndType(type);
    }

    @Override
    public List<ContentItem> findFeaturedContentByCategory(Category category) {
        return contentItemRepository.findByFeaturedTrueAndCategory(category);
    }

    // ── Content approval methods ──

    @Override
    public List<ContentItem> findContentByStatus(ContentStatus status) {
        // Returns all content items (poems, blogs, essays) with the given status
        return contentItemRepository.findByStatus(status);
    }

    @Override
    public List<ContentItem> findApprovedContent() {
        // Convenience method: returns only publicly visible content
        return contentItemRepository.findByStatus(ContentStatus.APPROVED);
    }
}
