package com.DavidsCode.CommunityWebPage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Essay extends ContentItem {
    
    public Essay() {}

    @Override
    public String getUrl() {
        return "/essay/" + getId();
    }
}
