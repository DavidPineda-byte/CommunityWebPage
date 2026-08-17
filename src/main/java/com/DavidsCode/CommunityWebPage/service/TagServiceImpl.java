package com.DavidsCode.CommunityWebPage.service;

import com.DavidsCode.CommunityWebPage.entity.Tag;
import com.DavidsCode.CommunityWebPage.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl {

    @Autowired
    private TagRepository tagRepository;

    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }
}
