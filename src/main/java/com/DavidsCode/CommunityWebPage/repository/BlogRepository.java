package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByTagsContaining(Tag tag);
    List<Blog> findByGenre(Genre genre);
    List<Blog> findByCategory(Category category);

    // Returns only blogs with a specific approval status
    List<Blog> findByStatus(ContentStatus status);
}
