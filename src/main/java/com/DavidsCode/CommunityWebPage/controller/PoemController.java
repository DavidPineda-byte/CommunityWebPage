package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.service.UserService;
import com.DavidsCode.CommunityWebPage.service.poemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/poem")
public class PoemController {
    @Autowired
    private poemServiceImpl poemServiceImpl;

    @GetMapping
    public String showPoems(Model model) {
        List<Poem> poems = poemServiceImpl.getAllPoems();
        model.addAttribute("poems", poems);
        return "displayPoems";

    }

    @GetMapping("/add")
    public String addPoem(Model model){
    model.addAttribute("poem", new Poem());
    return "addPoem";
    }

    @PostMapping("/add")
    public String addPoem(@ModelAttribute("poem") Poem poem){
        poemServiceImpl.addPoem(poem);
        return "redirect:/poem";
    }
}
