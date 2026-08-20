package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.FilteredOptionList;
import com.DavidsCode.CommunityWebPage.dto.Option;
import com.DavidsCode.CommunityWebPage.dto.OptionList;
import com.DavidsCode.CommunityWebPage.dto.PoemDraft;
import com.DavidsCode.CommunityWebPage.service.FluxImageAPI;
import com.DavidsCode.CommunityWebPage.service.GenreServiceImpl;
import com.DavidsCode.CommunityWebPage.service.ContentItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class APIRestController {

    @Autowired
    private ContentItemServiceImpl contentItemService;

    @Autowired
    private GenreServiceImpl genreService;

    @PostMapping("/poem/generate-image")
    public String generateImage(@RequestBody PoemDraft request) {
        String prompt = request.getPrompt();
        FluxImageAPI fluxImageAPI = new FluxImageAPI();
        return fluxImageAPI.generateImageURL(prompt).join();
    }

    @GetMapping("/api/options")
    public Map<String, ArrayList<Option>> getOptionsThenFilter(@RequestParam(name = "input") String filterBy) {
        OptionList optionList = new OptionList(contentItemService.findApprovedContent(), genreService.findAllGenres());
        FilteredOptionList filteredOptionList = new FilteredOptionList(optionList.getFullOptionsList(), filterBy);

        return Map.of("options", filteredOptionList.getFilteredOptionList());
    }
}