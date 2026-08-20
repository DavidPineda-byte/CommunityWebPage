package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.PublishObject;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.repository.ContentItemRepository;
import com.DavidsCode.CommunityWebPage.service.ContentItemServiceImpl;
import com.DavidsCode.CommunityWebPage.service.GenreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContentEditController {

    @Autowired
    private ContentItemServiceImpl contentItemService;
    
    @Autowired
    private ContentItemRepository contentItemRepository;

    @Autowired
    private GenreServiceImpl genreService;

    @GetMapping({"/content/{id}/edit", "/admin/content/{id}/edit"})
    public String showEditPage(@PathVariable Long id, Model model, org.springframework.security.core.Authentication authentication) {
        ContentItem item = contentItemService.findContentById(id);
        if (item == null) {
            return "redirect:/"; // or some error page
        }

        // Ownership check
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (authentication == null || !authentication.getName().equals(item.getAuthor()))) {
            return "redirect:/";
        }

        PublishObject editObj = new PublishObject();
        editObj.setId(item.getId());
        editObj.setTitle(item.getTitle());
        editObj.setAuthor(item.getAuthor());
        editObj.setBody(item.getBody());
        if (item.getGenre() != null) {
            editObj.setGenreId(item.getGenre().getId());
        }

        if (item instanceof Poem) {
            editObj.setTypeField("POEM");
        } else if (item instanceof Essay) {
            editObj.setTypeField("ESSAY");
        } else if (item instanceof Blog) {
            editObj.setTypeField("BLOG");
        }

        model.addAttribute("editObject", editObj);
        model.addAttribute("genres", genreService.findAllGenres());
        model.addAttribute("view", "editContentTemplate");
        return "main";
    }

    @PostMapping({"/content/edit", "/admin/content/edit"})
    public String saveEdit(@ModelAttribute("editObject") PublishObject editObj,
                           RedirectAttributes redirectAttributes,
                           org.springframework.security.core.Authentication authentication) {
        ContentItem item = contentItemService.findContentById(editObj.getId());
        
        // Ownership check
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (item != null && !isAdmin && (authentication == null || !authentication.getName().equals(item.getAuthor()))) {
            return "redirect:/";
        }

        if (item != null) {
            item.setTitle(editObj.getTitle());
            item.setAuthor(editObj.getAuthor());
            item.setBody(editObj.getBody());
            if (editObj.getGenreId() != null) {
                genreService.findGenreById(editObj.getGenreId()).ifPresent(item::setGenre);
            }
            contentItemRepository.save(item);
        }

        redirectAttributes.addFlashAttribute("success", "Content updated successfully!");
        return "redirect:" + (item != null ? item.getUrl() : "/");
    }
}
