package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.repository.CommentRepository;
import com.DavidsCode.CommunityWebPage.service.EssayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/essay")
public class EssayController {

    @Autowired
    private EssayServiceImpl essayService;

    @Autowired
    private CommentRepository commentRepository;

    // Published Essay Page
    @GetMapping("/{id}")
    public String readEssay(@PathVariable Long id, Model model) {
        Essay essay = essayService.findEssayById(id)
                .orElseThrow(() -> new RuntimeException("Essay not found"));

        model.addAttribute("essay", essay);
        model.addAttribute("comments", commentRepository.findByContentItemIdOrderByCreatedAtDesc(essay.getId()));
        model.addAttribute("view", "postedEssayFragment");
        return "main";
    }
}
