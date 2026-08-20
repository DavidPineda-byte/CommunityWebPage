package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    void saveBlog(Blog blog);
    List<Blog> findAllBlogs();
    Optional<Blog> findBlogById(Long id);
    List<Blog> findBlogsByTag(Tag tag);
    void deleteBlogById(Long id);
    Blog findFeaturedBlog();
    List<Blog> findByGenre(Genre genre);
    List<Blog> findByCategory(Category category);
}
