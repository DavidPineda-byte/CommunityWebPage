package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.repository.EssayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EssayServiceImpl implements EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Override
    public void saveEssay(Essay essay) {
        essayRepository.save(essay);
    }

    @Override
    public List<Essay> findAllEssays() {
        // Only return APPROVED essays for public-facing pages
        return essayRepository.findByStatus(ContentStatus.APPROVED);
    }

    @Override
    public Optional<Essay> findEssayById(Long id) {
        return essayRepository.findById(id);
    }

    @Override
    public List<Essay> findByGenre(Genre genre) {
        return essayRepository.findByGenreAndStatus(genre, ContentStatus.APPROVED);
    }

    @Override
    public List<Essay> findByCategory(Category category) {
        return essayRepository.findByCategory(category);
    }

    // Returns ALL essays regardless of status — used by the admin dashboard
    public List<Essay> findAllEssaysUnfiltered() {
        return essayRepository.findAll();
    }
}
