package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.repository.PoemGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    PoemGenreRepository poemGenreRepository;

    @Cacheable("genres")
    public List<Genre> findAllGenres() {
       return poemGenreRepository.findAll();
    }
    public Optional<Genre> findByName(String genreName) {
        return poemGenreRepository.findByName(genreName);
    }
    @CacheEvict(value = "genres", allEntries = true)
    public void  deleteGenreById(Long id) {
        poemGenreRepository.deleteById(id);
    }
    public Optional<Genre> findGenreById(Long id) {
        return poemGenreRepository.findById(id);
    }
    @CacheEvict(value = "genres", allEntries = true)
    public void deleteGenreByName(String genreName) {
        poemGenreRepository.deleteGenreByName(genreName);
    }
    @CacheEvict(value = "genres", allEntries = true)
    public void saveGenre(Genre genre) {
        poemGenreRepository.save(genre);
    }
    public List<Genre> findAllByCategory(Category category) {
        return poemGenreRepository.findAllByCategory(category);
    }
}
