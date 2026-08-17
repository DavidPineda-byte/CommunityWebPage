package com.DavidsCode.CommunityWebPage.dto;

import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class OptionList {

    private final ArrayList<Option> FulloptionsList;

    public OptionList(List<ContentItem> contentItems, List<Genre> genres){
        this.FulloptionsList = makeOptionList(contentItems, genres);
    }

    // Make an options list
    public ArrayList<Option> makeOptionList(List<ContentItem> contentItems, List<Genre> genres){

        ArrayList<Option> options = new ArrayList<>();

        if (contentItems != null) {
            for(ContentItem item : contentItems){
                Option option = makeIntoOption(item);
                options.add(option);
            }
        }

        if (genres != null) {
            for(Genre genre : genres){
                Option option = makeIntoOption(genre);
                options.add(option);
            }
        }
        return options;
    }

    public Option makeIntoOption(ContentItem item){
        Option option = new Option();
        option.setName(item.getTitle());
        option.setId(item.getId());
        option.setUrl(item.getUrl());

        return option;
    }

    public Option makeIntoOption(Genre genre){
        Option option = new Option();
        option.setName(genre.getName());
        option.setId(genre.getId());
        option.setUrl("/genre/" + genre.getId());
        return option;
    }
    public ArrayList<Option> getFullOptionsList(){
       return FulloptionsList;
    }
}


