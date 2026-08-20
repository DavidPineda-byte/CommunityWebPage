package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.service.GenreServiceImpl;
import com.DavidsCode.CommunityWebPage.service.poemServiceImpl;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.*;

@Controller
@RequestMapping("/poem")
public class PoemController {
    @Autowired
    private poemServiceImpl poemServiceImpl;
    @Autowired
    private GenreServiceImpl genreServiceImpl;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private com.DavidsCode.CommunityWebPage.service.TempPoemStorage tempPoemStorage;

    @Autowired
    private com.DavidsCode.CommunityWebPage.repository.CommentRepository commentRepository;

    @GetMapping("/add")
    public String addPoem(Model model, 
                          @RequestParam(required = false) String tempId,
                          @RequestParam(required = false) String genre){
        Poem poem = new Poem();
        
        if (tempId != null) {
            com.DavidsCode.CommunityWebPage.dto.PoemDraft draft = tempPoemStorage.get(tempId);
            if (draft != null) {
                poem.setTitle(draft.getTitle());
                poem.setAuthor(draft.getAuthor());
                poem.setBody(draft.getBody());
                genreServiceImpl.findByName(draft.getGenre()).ifPresent(poem::setGenre);
                poem.setImageUrl(draft.getImageUrl());
            }
        } else if (genre != null) {
            genreServiceImpl.findByName(genre).ifPresent(poem::setGenre);
        }
        
        List<Genre> genres = genreServiceImpl.findAllGenres();
        model.addAttribute("genres", genres);
        model.addAttribute("poem", poem);
        model.addAttribute("view", "addPoem");
        return "main";
    }


    @Value("${aws.s3.bucket}")
    private String bucketName;
    @PostMapping("/add")
    // this should just save the poem and the image.
    public String addPoem(
            @ModelAttribute("poem") Poem poem,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "generatedImageUrl", required = false) String generatedImageUrl,
            RedirectAttributes redirectAttributes
    ) throws StripeException {

        try {
            String key = UUID.randomUUID() + ".jpg";
            java.io.InputStream inputStream;
            String contentType;
            long size;

            if (image != null && !image.isEmpty()) {
                key = UUID.randomUUID() + "-" + image.getOriginalFilename();
                inputStream = image.getInputStream();
                contentType = image.getContentType();
                size = image.getSize();
            } else if (org.springframework.util.StringUtils.hasText(generatedImageUrl)) {
                byte[] imageBytes = kong.unirest.Unirest.get(generatedImageUrl).asBytes().getBody();
                inputStream = new java.io.ByteArrayInputStream(imageBytes);
                contentType = "image/jpeg";
                size = imageBytes.length;
            } else {
                throw new RuntimeException("No image provided");
            }

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(contentType)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            inputStream,
                            size
                    )
            );

            String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

            poem.setImageUrl(imageUrl);

            poemServiceImpl.addPoem(poem);

            redirectAttributes.addFlashAttribute("success", "Poem uploaded!");

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }

        return "redirect:/genre";
    }

 // Published Poem Page
    @GetMapping("/read/{id}")
    public String readPoem(@PathVariable Long id, Model model) {
        Poem poem = poemServiceImpl.poemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poem not found"));
        
        model.addAttribute("poem", poem);
        model.addAttribute("comments", commentRepository.findByContentItemIdOrderByCreatedAtDesc(id));
        model.addAttribute("view", "readPoem");
        return "main";
    }
}
