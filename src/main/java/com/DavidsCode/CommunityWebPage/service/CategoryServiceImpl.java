package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    class CategoryIterator {
        private Long id = 0L;

        public Category next() {
            Category category = categoryRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
            if(category == null) {
                return null;
            }
            id = category.getId();
            return category;
        }
    }

    @Override
    public Supplier<Category> CategoryFactory() {
        CategoryIterator categoryIterator = new CategoryIterator();
        return () -> categoryIterator.next();

    }
    @Override
    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

}
