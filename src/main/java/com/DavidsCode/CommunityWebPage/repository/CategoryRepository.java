package com.DavidsCode.CommunityWebPage.repository;


import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Category findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
