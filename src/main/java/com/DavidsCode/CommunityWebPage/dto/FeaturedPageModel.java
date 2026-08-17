package com.DavidsCode.CommunityWebPage.dto;

import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;

import java.util.List;
import java.util.Map;

public class FeaturedPageModel {

    private List<Blog> featuredBlogs;

    private Map<Category, List<ContentItem>> categoriesContentMaps;

    public FeaturedPageModel(List<Blog> featuredBlogs, Map<Category, List<ContentItem>> categoriesContentMaps) {
        this.featuredBlogs = featuredBlogs;
        this.categoriesContentMaps = categoriesContentMaps;
    }

    public List<Blog> getFeaturedBlogs() {
        return featuredBlogs;
    }

    public Map<Category, List<ContentItem>> getCategoriesContentMaps() {
        return categoriesContentMaps;
    }
}