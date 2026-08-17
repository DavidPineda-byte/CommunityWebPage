package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface EssayService {
    void saveEssay(Essay essay);
    List<Essay> findAllEssays();
    Optional<Essay> findEssayById(Long id);
    List<Essay> findByGenre(Genre genre);
    List<Essay> findByCategory(Category category);
}
