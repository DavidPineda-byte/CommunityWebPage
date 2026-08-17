package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    Optional<Genre> findByName(String name);
    List<Genre> findAllGenres();
    void deleteGenreById(Long id);
    void saveGenre(Genre genre);
    void deleteGenreByName(String name);
    List<Genre> findAllByCategory(Category category);
}
