package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.exceptions.ResourceNotFoundException;
import com.DavidsCode.CommunityWebPage.repository.PoemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class poemServiceImpl implements poemService {

   @Autowired
   public PoemRepository poemRepository;

    @Override
    public Poem getPoemById(Long id) {

        Poem poem = poemRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Poem with id " + id + " not found"));

        return poem;
    }

    @Override
    public Poem getPoemByAuthor(String author) {
        return poemRepository.getPoemByAuthor(author);
    }

    @Override
    public Poem getPoemByTitle(String title) {

        Poem poem = poemRepository.getPoemByTitle(title);
        if(poem == null) {
            throw new ResourceNotFoundException("Poem with title " + title + " not found");

        }
        return poem;
    }
    public void addPoem(Poem poem){
        poemRepository.save(poem);
    }
    public List<Poem> getAllPoems(){
        // Only return APPROVED poems for public-facing pages
        return poemRepository.findByStatus(ContentStatus.APPROVED);
    }

    public List<Poem> getPoemsByGenre(Genre genre){
       return poemRepository.findByGenreAndStatus(genre, ContentStatus.APPROVED);
    }

    public List<Poem> getPoemsByCategory(Category category){
        return poemRepository.findByCategory(category);
    }

    // Returns ALL poems regardless of status — used by the admin dashboard
    public List<Poem> getAllPoemsUnfiltered() {
        return poemRepository.findAll();
    }
}
