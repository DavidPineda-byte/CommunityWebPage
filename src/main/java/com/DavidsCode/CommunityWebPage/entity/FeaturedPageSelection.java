package com.DavidsCode.CommunityWebPage.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class FeaturedPageSelection {
    @Id
    private Long id = 1L;

    @ElementCollection(fetch = FetchType.EAGER)
    private java.util.Set<Long> blogIds;

    private Long categoryId1;
    private Long categoryId2;
    private Long categoryId3;

    @ElementCollection(fetch = FetchType.EAGER)
    private java.util.Set<Long> contentIds1;

    @ElementCollection(fetch = FetchType.EAGER)
    private java.util.Set<Long> contentIds2;

    @ElementCollection(fetch = FetchType.EAGER)
    private java.util.Set<Long> contentIds3;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public java.util.Set<Long> getBlogIds() { return blogIds; }
    public void setBlogIds(java.util.Set<Long> blogIds) { this.blogIds = blogIds; }
    public Long getCategoryId1() { return categoryId1; }
    public void setCategoryId1(Long categoryId1) { this.categoryId1 = categoryId1; }
    public Long getCategoryId2() { return categoryId2; }
    public void setCategoryId2(Long categoryId2) { this.categoryId2 = categoryId2; }
    public Long getCategoryId3() { return categoryId3; }
    public void setCategoryId3(Long categoryId3) { this.categoryId3 = categoryId3; }
    public java.util.Set<Long> getContentIds1() { return contentIds1; }
    public void setContentIds1(java.util.Set<Long> contentIds1) { this.contentIds1 = contentIds1; }
    public java.util.Set<Long> getContentIds2() { return contentIds2; }
    public void setContentIds2(java.util.Set<Long> contentIds2) { this.contentIds2 = contentIds2; }
    public java.util.Set<Long> getContentIds3() { return contentIds3; }
    public void setContentIds3(java.util.Set<Long> contentIds3) { this.contentIds3 = contentIds3; }
}
