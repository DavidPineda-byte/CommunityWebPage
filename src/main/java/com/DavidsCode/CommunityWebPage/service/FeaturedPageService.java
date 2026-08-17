package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.dto.FeaturedPageModel;
import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeaturedPageService {

    @Autowired
    ContentItemServiceImpl contentItemService;
    @Autowired
    CategoryService categoryService;
    private FeaturedPageModel featuredPageModel;

    @Autowired
    private com.DavidsCode.CommunityWebPage.repository.FeaturedPageSelectionRepository featuredPageSelectionRepository;
    @Autowired
    private com.DavidsCode.CommunityWebPage.repository.ContentItemRepository contentItemRepository;
    @Autowired
    private com.DavidsCode.CommunityWebPage.repository.CategoryRepository categoryRepository;

    public FeaturedPageModel getFeaturedPage() {
        if (featuredPageModel == null) {
            BuildFeaturedPageModel();
        }
        return featuredPageModel;
    }

    public void setFeaturedPageModel(FeaturedPageModel featuredPageModel) {
        this.featuredPageModel = featuredPageModel;
    }

    public void BuildFeaturedPageModel() {
        // Load the saved selection from the database
        com.DavidsCode.CommunityWebPage.entity.FeaturedPageSelection selection = featuredPageSelectionRepository.findById(1L).orElse(null);
        if (selection == null) {
            this.featuredPageModel = new FeaturedPageModel(new java.util.ArrayList<>(), new java.util.LinkedHashMap<>());
            return;
        }

        List<Blog> featuredBlogs = new java.util.ArrayList<>();
        if (selection.getBlogIds() != null) {
            for (Long blogId : selection.getBlogIds()) {
                contentItemRepository.findById(blogId).ifPresent(item -> {
                    if (item instanceof Blog) featuredBlogs.add((Blog) item);
                });
            }
        }

        Map<Category, List<ContentItem>> map = new java.util.LinkedHashMap<>();
        
        if (selection.getCategoryId1() != null) {
            categoryRepository.findById(selection.getCategoryId1()).ifPresent(cat -> {
                List<ContentItem> items = new java.util.ArrayList<>();
                if (selection.getContentIds1() != null) {
                    for (Long id : selection.getContentIds1()) {
                        contentItemRepository.findById(id).ifPresent(items::add);
                    }
                }
                map.put(cat, items);
            });
        }
        
        if (selection.getCategoryId2() != null) {
            categoryRepository.findById(selection.getCategoryId2()).ifPresent(cat -> {
                List<ContentItem> items = new java.util.ArrayList<>();
                if (selection.getContentIds2() != null) {
                    for (Long id : selection.getContentIds2()) {
                        contentItemRepository.findById(id).ifPresent(items::add);
                    }
                }
                map.put(cat, items);
            });
        }
        
        if (selection.getCategoryId3() != null) {
            categoryRepository.findById(selection.getCategoryId3()).ifPresent(cat -> {
                List<ContentItem> items = new java.util.ArrayList<>();
                if (selection.getContentIds3() != null) {
                    for (Long id : selection.getContentIds3()) {
                        contentItemRepository.findById(id).ifPresent(items::add);
                    }
                }
                map.put(cat, items);
            });
        }

        this.featuredPageModel = new FeaturedPageModel(featuredBlogs, map);
    }
}
