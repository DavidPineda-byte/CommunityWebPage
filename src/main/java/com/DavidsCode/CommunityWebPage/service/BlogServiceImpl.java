package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.*;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ContentItemServiceImpl contentItemService;

    @Override
    public void saveBlog(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public List<Blog> findAllBlogs() {
        // Only return APPROVED blogs for public-facing pages
        return blogRepository.findByStatus(ContentStatus.APPROVED);
    }

    @Override
    public Optional<Blog> findBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public List<Blog> findBlogsByTag(Tag tag) {
        return blogRepository.findByTagsContaining(tag);
    }

    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog findFeaturedBlog() {
        List<Blog> featured = contentItemService.findFeaturedContentByType(Blog.class);
        return featured.isEmpty() ? null : featured.get(0);
    }


    @Override
    public List<Blog> findByGenre(Genre genre) {
        return blogRepository.findByGenre(genre);
    }

    @Override
    public List<Blog> findByCategory(Category category) {
        return blogRepository.findByCategory(category);
    }

    // Returns ALL blogs regardless of status — used by the admin dashboard
    public List<Blog> findAllBlogsUnfiltered() {
        return blogRepository.findAll();
    }
}
