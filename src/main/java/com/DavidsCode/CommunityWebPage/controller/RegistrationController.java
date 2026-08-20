package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.User;
import com.DavidsCode.CommunityWebPage.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("view", "register");
        return "main";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userServiceImpl.registerUser(user);
            model.addAttribute("message", "Registration successful! Please login.");
            model.addAttribute("view", "login");
            return "main";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("view", "register");
            return "main";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration. Please try again later.");
            model.addAttribute("view", "register");
            return "main";
        }
    }
}

