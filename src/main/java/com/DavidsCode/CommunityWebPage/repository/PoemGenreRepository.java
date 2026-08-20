package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PoemGenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
    void deleteGenreByName(String name);
    List<Genre> findAllByCategory(Category category );
}


