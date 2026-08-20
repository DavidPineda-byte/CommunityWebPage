package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.FeaturedPageModel;
import com.DavidsCode.CommunityWebPage.service.FeaturedPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/featured")
public class FeaturedPageController {
private final FeaturedPageService featuredPageSerivce;
public FeaturedPageController(FeaturedPageService featuredPageService){
    this.featuredPageSerivce = featuredPageService;
}

     @GetMapping
    public String featuredPage(Model model) {


         FeaturedPageModel featuredPageModel = featuredPageSerivce.getFeaturedPage();

         model.addAttribute("view", "featuredPage");
         model.addAttribute("page", featuredPageModel);
         return "main";
     }
}
