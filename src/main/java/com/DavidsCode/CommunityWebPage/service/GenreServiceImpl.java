package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.repository.poemGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    poemGenreRepository poemGenreRepository;

    public List<Genre> findAllGenres() {
       return poemGenreRepository.findAll();
    }
    public Optional<Genre> findByName(String genreName) {
        return poemGenreRepository.findByName(genreName);
    }
    public void  deleteGenreById(Long id) {
        poemGenreRepository.deleteById(id);
    }
    public Optional<Genre> findGenreById(Long id) {
        return poemGenreRepository.findById(id);
    }
    public void deleteGenreByName(String genreName) {
        poemGenreRepository.deleteGenreByName(genreName);
    }
    public void saveGenre(Genre genre) {
        poemGenreRepository.save(genre);
    }
}
