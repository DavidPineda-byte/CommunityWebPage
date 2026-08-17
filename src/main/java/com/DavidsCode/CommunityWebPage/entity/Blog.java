package com.DavidsCode.CommunityWebPage.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Blog extends ContentItem {

    @ManyToMany
    @JoinTable(
            name="blog_tag",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<Tag> tags;

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String getUrl() {
        return "/blog/" + getId();
    }
}
