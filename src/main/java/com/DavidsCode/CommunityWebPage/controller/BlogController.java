package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.service.BlogServiceImpl;
import com.DavidsCode.CommunityWebPage.service.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import com.DavidsCode.CommunityWebPage.service.UserServiceImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.UUID;
import java.io.IOException;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // Show the draft form (ADMIN only — enforced in SecurityConfig)
    @GetMapping("/draft")
    public String showBlogDraft(Model model, Authentication authentication) {
        Blog blog = new Blog();
        model.addAttribute("tags", tagService.findAllTags());
        
        // Pre-fill author from logged-in user's display name
        if (authentication != null) {
            com.DavidsCode.CommunityWebPage.entity.User u = userService.getUserByUsername(authentication.getName());
            if (u != null && u.getDisplayName() != null && !u.getDisplayName().isBlank()) {
                blog.setAuthor(u.getDisplayName());
            } else {
                blog.setAuthor(authentication.getName());
            }
        }
        
        model.addAttribute("blog", blog);
        model.addAttribute("view", "blogDraftTemplate");
        return "main";
    }

    // Save the draft blog
    @PostMapping("/draft")
    @org.springframework.transaction.annotation.Transactional
    public String saveBlogDraft(@ModelAttribute("blog") Blog blog,
                                @RequestParam("headerImage") MultipartFile headerImage,
                                RedirectAttributes redirectAttributes) {
        try {
            // Pre-save to get the ID for S3 path
            blogService.saveBlog(blog);
            Long blogId = blog.getId();

            // Handle Header Image
            if (headerImage != null && !headerImage.isEmpty()) {
                String key = "BLOG/" + blogId + "/header-" + UUID.randomUUID() + ".png";
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build(),
                        RequestBody.fromInputStream(headerImage.getInputStream(), headerImage.getSize())
                );
                blog.setImageUrl("https://" + bucketName + ".s3.amazonaws.com/" + key);
            }

            // Parse body and upload base64 images
            if (blog.getBody() != null) {
                Document document = Jsoup.parse(blog.getBody());
                Elements images = document.select("img");
                for (Element image : images) {
                    String src = image.attr("src");
                    if (src.startsWith("data:image")) {
                        String base64Data = src.split(",")[1];
                        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                        String key = "BLOG/" + blogId + "/" + UUID.randomUUID() + ".png";
                        s3Client.putObject(
                                PutObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(key)
                                        .build(),
                                RequestBody.fromBytes(imageBytes)
                        );
                        String newImageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;
                        image.attr("src", newImageUrl);
                    }
                }
                blog.setBody(document.html());
            }

            // Save updated blog with S3 URLs
            blogService.saveBlog(blog);

            redirectAttributes.addFlashAttribute("success", "Blog saved!");
            return "redirect:/blog/" + blog.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save blog: " + e.getMessage());
            return "redirect:/blog/draft";
        }
    }

    // View a posted blog by ID
    @GetMapping("/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.findBlogById(id)
                .orElseThrow(() -> new com.DavidsCode.CommunityWebPage.exceptions.ResourceNotFoundException("Blog not found"));
        model.addAttribute("blog", blog);
        model.addAttribute("recentBlogs", blogService.findAllBlogs());
        model.addAttribute("view", "postedBlogTemplate");
        return "main";
    }

    // Blog listing page (all blogs)
    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogService.findAllBlogs());
        model.addAttribute("featuredBlog", blogService.findFeaturedBlog());
        model.addAttribute("view", "blogListingPage");
        return "main";
    }
}
