package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;

import java.util.List;
import java.util.Optional;

public interface poemService {

    Poem getPoemByAuthor(String author);
    Poem getPoemById(Long id);
    Poem getPoemByTitle(String title);
    void addPoem(Poem poem);
    List<Poem> getAllPoems();
    List<Poem> getPoemsByGenre(Genre genre);
    List<Poem> getPoemsByCategory(Category category);
}
