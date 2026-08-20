package com.DavidsCode.CommunityWebPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(org.springframework.ui.Model model) {
        model.addAttribute("view", "login");
        return "main";
    }

}