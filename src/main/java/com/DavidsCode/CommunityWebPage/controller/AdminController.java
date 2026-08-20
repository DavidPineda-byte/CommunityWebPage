package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.dto.FeaturedPageModel;
import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.ContentItem;
import com.DavidsCode.CommunityWebPage.entity.ContentStatus;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Tag;
import com.DavidsCode.CommunityWebPage.repository.CategoryRepository;
import com.DavidsCode.CommunityWebPage.repository.ContentItemRepository;
import com.DavidsCode.CommunityWebPage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private BlogServiceImpl blogService;

    @Autowired
    private poemServiceImpl poemService;

    @Autowired
    private EssayServiceImpl essayService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private ContentItemServiceImpl contentItemService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentItemRepository contentItemRepository;
    @Autowired
    private FeaturedPageService featuredPageService;
    @Autowired
    private com.DavidsCode.CommunityWebPage.repository.FeaturedPageSelectionRepository featuredPageSelectionRepository;
    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("genres", genreService.findAllGenres());
        model.addAttribute("tags", tagService.findAllTags());
        // Admin sees ALL content regardless of approval status
        model.addAttribute("poems", poemService.getAllPoemsUnfiltered());
        model.addAttribute("blogs", blogService.findAllBlogsUnfiltered());
        model.addAttribute("essays", essayService.findAllEssaysUnfiltered());

        // Pending content queue — items awaiting admin review
        model.addAttribute("pendingContent", contentItemService.findContentByStatus(ContentStatus.PENDING));
        model.addAttribute("featuredBlog", blogService.findFeaturedBlog());
        model.addAttribute("categories", categoryService.findAllCategories());
        List<ContentItem> allContentItems = contentItemService.getAllContentItems();
        model.addAttribute("contentItems", allContentItems);
        model.addAttribute("allContent", allContentItems); // used by approved and rejected tabs

        model.addAttribute("view", "adminDashboardTemplate");
        return "main";
    }

    // Genre Management
    @PostMapping("/genre/add")
    public String addGenre(
            @RequestParam String name,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {
        try {
            Genre genre = new Genre();
            genre.setName(name);

            // Associate with a category if one was selected
            if (categoryId != null) {
                Category category = categoryService.findCategoryById(categoryId);
                if (category != null) {
                    genre.setCategory(category);
                }
            }

            // Upload image to S3 (same pattern as BlogController/PoemController)
            if (imageFile != null && !imageFile.isEmpty()) {
                String key = "GENRE/" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .build(),
                        RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize())
                );
                genre.setImageUrl("https://" + bucketName + ".s3.amazonaws.com/" + key);
            } else if (imageUrl != null && !imageUrl.isBlank()) {
                genre.setImageUrl(imageUrl);
            }

            genreService.saveGenre(genre);
            redirectAttributes.addFlashAttribute("success", "Genre added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding genre: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // Category Management
    @PostMapping("/category/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              RedirectAttributes redirectAttributes) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategoryById(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/genre/delete/{id}")
    public String deleteGenre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            genreService.deleteGenreById(id);
            redirectAttributes.addFlashAttribute("success", "Genre deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting genre: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // Tag Management
    @PostMapping("/tag/add")
    public String addTag(@RequestParam String name, @RequestParam(required = false) String description, RedirectAttributes redirectAttributes) {
        try {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setDescription(description);
            tagService.saveTag(tag);
            redirectAttributes.addFlashAttribute("success", "Tag added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding tag: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/tag/delete/{id}")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTagById(id);
            redirectAttributes.addFlashAttribute("success", "Tag deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting tag: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Approve a pending content item
    // Changes the content's status from PENDING to APPROVED,
    // making it visible on the public site.
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/content/{id}/approve")
    public String approveContent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ContentItem item = contentItemService.findContentById(id);
        item.setStatus(ContentStatus.APPROVED);
        contentItemRepository.save(item);
        redirectAttributes.addFlashAttribute("successMessage", "Content approved!");
        return "redirect:/admin";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Reject a pending content item
    // Changes the content's status from PENDING to REJECTED,
    // keeping it hidden from the public site.
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/content/{id}/reject")
    public String rejectContent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ContentItem item = contentItemService.findContentById(id);
        item.setStatus(ContentStatus.REJECTED);
        contentItemRepository.save(item);
        redirectAttributes.addFlashAttribute("successMessage", "Content rejected.");
        return "redirect:/admin";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Toggle content visibility (APPROVED <-> REJECTED)
    // Allows the admin to hide previously approved content
    // or restore previously rejected content.
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/content/{id}/toggle")
    public String toggleContentVisibility(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        ContentItem item = contentItemService.findContentById(id);
        if (item.getStatus() == ContentStatus.APPROVED) {
            item.setStatus(ContentStatus.REJECTED);
            redirectAttributes.addFlashAttribute("successMessage", "Content hidden from public.");
        } else {
            item.setStatus(ContentStatus.APPROVED);
            redirectAttributes.addFlashAttribute("successMessage", "Content made public.");
        }
        contentItemRepository.save(item);
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Make Featured
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/blog/{id}/feature")
    public String featureContent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ContentItem item = contentItemService.findContentById(id);
        if (item != null) {
            item.setFeatured(true);
            contentItemRepository.save(item);
            redirectAttributes.addFlashAttribute("successMessage", "Content marked as featured!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Content not found.");
        }
        return "redirect:/admin";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Permanently delete a content item
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/content/{id}/delete")
    public String deleteContent(@PathVariable Long id, @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes redirectAttributes) {
        try {
            contentItemRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Content permanently deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting content: " + e.getMessage());
        }
        return "redirect:" + (referer != null ? referer : "/admin");
    }

    // ────────────────────────────────────────────────────────────────────────────
    // GET: Manage all content page
    // ────────────────────────────────────────────────────────────────────────────
    @GetMapping("/content")
    public String manageAllContent(Model model) {
        model.addAttribute("allContent", contentItemService.getAllContentItems());
        model.addAttribute("genres", genreService.findAllGenres());
        model.addAttribute("view", "adminContentManagementTemplate");
        return "main";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Move content to another genre
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/content/{id}/moveGenre")
    public String moveGenre(@PathVariable Long id, @RequestParam Long genreId, RedirectAttributes redirectAttributes) {
        try {
            ContentItem item = contentItemService.findContentById(id);
            Genre newGenre = genreService.findGenreById(genreId)
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            
            item.setGenre(newGenre);
            // category is automatically set by item.setGenre()
            contentItemRepository.save(item);
            
            redirectAttributes.addFlashAttribute("successMessage", "Content moved to genre: " + newGenre.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error moving content: " + e.getMessage());
        }
        return "redirect:/admin/content";
    }

    // ────────────────────────────────────────────────────────────────────────────
    // POST: Update a genre's name and/or image
    // ────────────────────────────────────────────────────────────────────────────
    @PostMapping("/genre/update/{id}")
    public String updateGenre(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {
        try {
            Genre genre = genreService.findGenreById(id)
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            if (name != null && !name.isBlank()) {
                genre.setName(name);
            }
            if (categoryId != null) {
                Category category = categoryService.findCategoryById(categoryId);
                if (category != null) {
                    genre.setCategory(category);
                }
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String key = "GENRE/" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
                s3Client.putObject(
                        PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                        RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize())
                );
                genre.setImageUrl("https://" + bucketName + ".s3.amazonaws.com/" + key);
            } else if (imageUrl != null && !imageUrl.isBlank()) {
                genre.setImageUrl(imageUrl);
            }
            genreService.saveGenre(genre);
            redirectAttributes.addFlashAttribute("success", "Genre updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating genre: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // Featured Page Builder
    @PostMapping("/featured/save")
    public String saveFeaturedPage(
            @RequestParam List<Long> blogIds,
            @RequestParam Long categoryId1,
            @RequestParam Long categoryId2,
            @RequestParam Long categoryId3,
            @RequestParam List<Long> contentIds1,
            @RequestParam List<Long> contentIds2,
            @RequestParam List<Long> contentIds3,
            RedirectAttributes redirectAttributes) {
        try {
            // Build blog list from selected IDs
            List<com.DavidsCode.CommunityWebPage.entity.Blog> featuredBlogs = new ArrayList<>();
            for (Long blogId : blogIds) {
                contentItemRepository.findById(blogId).ifPresent(item -> {
                    if (item instanceof com.DavidsCode.CommunityWebPage.entity.Blog) {
                        featuredBlogs.add((com.DavidsCode.CommunityWebPage.entity.Blog) item);
                    }
                });
            }

            // Build category-content map from selected IDs
            Map<Category, List<ContentItem>> categoriesContentMap = new LinkedHashMap<>();

            Category cat1 = categoryRepository.findById(categoryId1)
                    .orElseThrow(() -> new RuntimeException("Category 1 not found"));
            Category cat2 = categoryRepository.findById(categoryId2)
                    .orElseThrow(() -> new RuntimeException("Category 2 not found"));
            Category cat3 = categoryRepository.findById(categoryId3)
                    .orElseThrow(() -> new RuntimeException("Category 3 not found"));

            categoriesContentMap.put(cat1, contentItemRepository.findAllById(contentIds1)
                    .stream().map(item -> (ContentItem) item).collect(Collectors.toList()));
            categoriesContentMap.put(cat2, contentItemRepository.findAllById(contentIds2)
                    .stream().map(item -> (ContentItem) item).collect(Collectors.toList()));
            categoriesContentMap.put(cat3, contentItemRepository.findAllById(contentIds3)
                    .stream().map(item -> (ContentItem) item).collect(Collectors.toList()));

            FeaturedPageModel pageModel = new FeaturedPageModel(featuredBlogs, categoriesContentMap);
            featuredPageService.setFeaturedPageModel(pageModel);

            // Persist the selection to the database so it survives server restarts
            com.DavidsCode.CommunityWebPage.entity.FeaturedPageSelection selection = 
                featuredPageSelectionRepository.findById(1L).orElse(new com.DavidsCode.CommunityWebPage.entity.FeaturedPageSelection());
            selection.setBlogIds(blogIds == null ? new java.util.HashSet<>() : new java.util.HashSet<>(blogIds));
            selection.setCategoryId1(categoryId1);
            selection.setCategoryId2(categoryId2);
            selection.setCategoryId3(categoryId3);
            selection.setContentIds1(contentIds1 == null ? new java.util.HashSet<>() : new java.util.HashSet<>(contentIds1));
            selection.setContentIds2(contentIds2 == null ? new java.util.HashSet<>() : new java.util.HashSet<>(contentIds2));
            selection.setContentIds3(contentIds3 == null ? new java.util.HashSet<>() : new java.util.HashSet<>(contentIds3));
            featuredPageSelectionRepository.save(selection);

            redirectAttributes.addFlashAttribute("success", "Featured page updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving featured page: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
