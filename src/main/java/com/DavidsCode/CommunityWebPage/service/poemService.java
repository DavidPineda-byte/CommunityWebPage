package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Poem;

import java.util.List;
import java.util.Optional;

public interface poemService {

    public Poem getPoemByAuthor(String author);
    public Poem getPoemById(Long id);
    public Poem getPoemByTitle(String title);
    public void addPoem(Poem poem);
    public List<Poem> getAllPoems();
}
