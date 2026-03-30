package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.exceptions.ResourceNotFoundException;
import com.DavidsCode.CommunityWebPage.repository.poemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class poemServiceImpl implements poemService {

   @Autowired
   public poemRepository poemRepository;

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
        return poemRepository.findAll();
    }
}
