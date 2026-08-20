package com.DavidsCode.CommunityWebPage.entity;


import jakarta.persistence.*;

@Entity
public class Poem extends ContentItem {
    @Override
    public String getUrl() {
        return "/poem/read/" + getId();
    }
}
