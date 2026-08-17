package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.User;
import com.DavidsCode.CommunityWebPage.repository.ContentItemRepository;
import com.DavidsCode.CommunityWebPage.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/my-works")
public class MyWorksController {

    @Autowired
    private ContentItemRepository contentItemRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String myWorks(Authentication authentication, Model model) {
        String authorName = authentication.getName();
        User u = userService.getUserByUsername(authorName);
        if (u != null && u.getDisplayName() != null && !u.getDisplayName().isBlank()) {
            authorName = u.getDisplayName();
        }

        List<ContentItem> myItems = contentItemRepository.findByAuthorOrderByCreatedAtDesc(authorName);
        model.addAttribute("myItems", myItems);
        model.addAttribute("view", "myWorksTemplate");
        return "main";
    }
}
