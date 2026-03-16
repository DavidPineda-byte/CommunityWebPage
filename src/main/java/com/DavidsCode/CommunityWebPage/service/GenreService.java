package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    public Optional<Genre> findByName(String name);
    public List<Genre> findAllGenres();
    public void deleteGenreById(Long id);
    public void saveGenre(Genre genre);
    public void deleteGenreByName(String name);
}
