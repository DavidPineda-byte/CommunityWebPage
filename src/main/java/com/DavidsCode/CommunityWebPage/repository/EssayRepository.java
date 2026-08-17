package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {
    List<Essay> findByGenre(Genre genre);
    List<Essay> findByGenreAndStatus(Genre genre, ContentStatus status);
    List<Essay> findByCategory(Category category);

    // Returns only essays with a specific approval status
    List<Essay> findByStatus(ContentStatus status);
}
