package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Comment;
import com.DavidsCode.CommunityWebPage.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/add")
    public String addComment(
            @RequestParam Long contentItemId,
            @RequestParam String body,
            @RequestParam String redirectUrl,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (body == null || body.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Comment cannot be empty.");
            return "redirect:" + redirectUrl;
        }

        Comment comment = new Comment();
        comment.setContentItemId(contentItemId);
        comment.setBody(body.trim());
        comment.setAuthor(authentication != null ? authentication.getName() : "Anonymous");
        commentRepository.save(comment);

        redirectAttributes.addFlashAttribute("success", "Comment posted!");
        return "redirect:" + redirectUrl;
    }
}
