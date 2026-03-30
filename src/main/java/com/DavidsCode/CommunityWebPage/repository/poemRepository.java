package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Poem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface poemRepository extends JpaRepository<Poem, Long> {
    Poem getPoemByAuthor(String author);
    Poem getPoemByTitle(String title);
    List<Poem> findByGenre(String genre);
}

