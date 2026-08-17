package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.PublishObject;
import com.DavidsCode.CommunityWebPage.entity.Blog;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/publish")
public class PublishController {
    @Autowired
    private S3Client s3Client;

    @Autowired
    private poemServiceImpl poemService;

    @Autowired
    private EssayServiceImpl essayService;

    @Autowired
    private GenreServiceImpl genreService;
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String showPublishPage(Model model, Authentication authentication) {
        PublishObject publishObject = new PublishObject();
        
        // Pre-fill author from logged-in user's display name
        if (authentication != null) {
            com.DavidsCode.CommunityWebPage.entity.User u = userService.getUserByUsername(authentication.getName());
            if (u != null && u.getDisplayName() != null && !u.getDisplayName().isBlank()) {
                publishObject.setAuthor(u.getDisplayName());
            } else {
                publishObject.setAuthor(authentication.getName());
            }
        }
        
        model.addAttribute("publishObject", publishObject);
        model.addAttribute("genres", genreService.findAllGenres());
        model.addAttribute("view", "publishTemplate");
        return "main";
    }

    @Value("${aws.s3.bucket}")
    private String bucketName;
    
    @PostMapping("/save")
    @org.springframework.transaction.annotation.Transactional
    public String savePublication(
            @ModelAttribute("publishObject") PublishObject publishObject,
            @RequestParam("headerImage") org.springframework.web.multipart.MultipartFile headerImage,
            RedirectAttributes redirectAttributes,
            org.springframework.security.core.Authentication authentication) {

       String htmlContent = publishObject.getBody();
       System.out.println(htmlContent);
       Document document = Jsoup.parse(htmlContent);
       Elements images = document.select("img");

       Object finalPublishType;

       Long publishObjectId = null;

        Poem poem = null;
        Essay essay = null;
       if("POEM".equals(publishObject.getTypeField())){
            poem = new Poem();
            poem.setStatus(ContentStatus.PENDING); // prevent auto-approval on first save
           poemService.addPoem(poem);
           publishObjectId = poem.getId();
       }
       if("ESSAY".equals(publishObject.getTypeField())){
           essay = new Essay();
           essay.setStatus(ContentStatus.PENDING); // prevent auto-approval on first save
           essayService.saveEssay(essay);
           publishObjectId = essay.getId();
       }

       // Upload Header Image to S3
       String headerImageUrl = null;
       if (headerImage != null && !headerImage.isEmpty()) {
           try {
               String headerKey = publishObject.getTypeField() + "/" + publishObjectId + "/header-" + UUID.randomUUID() + ".png";
               s3Client.putObject(
                       PutObjectRequest.builder()
                               .bucket(bucketName)
                               .key(headerKey)
                               .build(),
                       RequestBody.fromInputStream(headerImage.getInputStream(), headerImage.getSize())
               );
               headerImageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + headerKey;
           } catch (Exception e) {
               e.printStackTrace();
           }
       }




       for(Element image: images){
           String src = image.attr("src");
           
           // Only process base64 data URIs
           if (src != null && src.startsWith("data:image")) {
               System.out.println("found Image");
               //decode base 64
               String base64Data = src.split(",")[1];
               byte[] imageBytes = Base64.getDecoder().decode(base64Data);

               String key = publishObject.getTypeField() + "/" + publishObjectId + "/" + UUID.randomUUID() + ".png";

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
        if (poem != null) {
            poem.setBody(document.html());
            poem.setAuthor(publishObject.getAuthor());
            poem.setTitle(publishObject.getTitle());
            if (headerImageUrl != null) {
                poem.setImageUrl(headerImageUrl);
            }
            Genre genre = genreService.findGenreById(publishObject.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            poem.setGenre(genre);

            // ── Set approval status based on user role ──
            // Admin-submitted content is automatically approved;
            // all other users' content goes into the pending queue for review.
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                poem.setStatus(ContentStatus.APPROVED);
            } else {
                poem.setStatus(ContentStatus.PENDING);
            }

            poemService.addPoem(poem);
        }

        if (essay != null) {
            essay.setBody(document.html());
            essay.setAuthor(publishObject.getAuthor());
            essay.setTitle(publishObject.getTitle());
            if (headerImageUrl != null) {
                essay.setImageUrl(headerImageUrl);
            }
            Genre genre = genreService.findGenreById(publishObject.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Genre not found"));

            essay.setGenre(genre);

            // ── Set approval status based on user role ──
            // Admin-submitted content is automatically approved;
            // all other users' content goes into the pending queue for review.
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                essay.setStatus(ContentStatus.APPROVED);
            } else {
                essay.setStatus(ContentStatus.PENDING);
            }

            essayService.saveEssay(essay);
        }

        if ("POEM".equals(publishObject.getTypeField())) {
            redirectAttributes.addFlashAttribute("success", "Poem submitted! It will be reviewed before appearing on the site.");
        } else if ("ESSAY".equals(publishObject.getTypeField())) {
            redirectAttributes.addFlashAttribute("success", "Essay submitted! It will be reviewed before appearing on the site.");
        }
        return "redirect:/my-works";
    }


    @GetMapping("/blog")
    public String blogDraft(Model model){

        model.addAttribute("blog", new Blog());
        model.addAttribute("tags", tagService.findAllTags());
        model.addAttribute("view", "blogDraftTemplate");
        return "main";
    }
}
