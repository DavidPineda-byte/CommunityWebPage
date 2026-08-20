package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;

import java.util.List;
import java.util.function.Supplier;

public interface CategoryService {
    Supplier<Category> CategoryFactory();
    List<Category> findAllCategories();
    Category saveCategory(Category category);
    void deleteCategoryById(Long id);
    Category findCategoryById(Long id);
}
