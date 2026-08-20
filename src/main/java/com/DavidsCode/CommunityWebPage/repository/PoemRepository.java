package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoemRepository extends JpaRepository<Poem, Long> {
    Poem getPoemByAuthor(String author);
    Poem getPoemByTitle(String title);
    List<Poem> findByGenre(Genre genre);
    List<Poem> findByGenreAndStatus(Genre genre, ContentStatus status);
    List<Poem> findByCategory(Category category); 

    // Returns only poems with a specific approval status
    List<Poem> findByStatus(ContentStatus status);
}

