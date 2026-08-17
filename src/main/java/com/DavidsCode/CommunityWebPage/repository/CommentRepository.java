package com.DavidsCode.CommunityWebPage.repository;

import com.DavidsCode.CommunityWebPage.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContentItemIdOrderByCreatedAtDesc(Long contentItemId);
}
