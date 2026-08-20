package com.DavidsCode.CommunityWebPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/")
    public String showHomePage(org.springframework.ui.Model model) {
        model.addAttribute("view", "home");
        return "main";
    }
}
