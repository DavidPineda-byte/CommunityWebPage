package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface poemGenreRepository extends JpaRepository<Genre, Long> {
    public Optional<Genre> findByName(String name);
    public void deleteGenreByName(String name);
}


