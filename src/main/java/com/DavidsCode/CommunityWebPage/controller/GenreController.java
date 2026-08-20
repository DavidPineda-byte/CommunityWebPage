package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Category;
import com.DavidsCode.CommunityWebPage.entity.Essay;
import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.service.CategoryServiceImpl;
import com.DavidsCode.CommunityWebPage.service.EssayServiceImpl;
import com.DavidsCode.CommunityWebPage.service.GenreServiceImpl;
import com.DavidsCode.CommunityWebPage.service.poemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;

@Controller
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreServiceImpl genreServiceImpl;

    @Autowired
    private poemServiceImpl poemServiceImpl;

    @Autowired
    private EssayServiceImpl essayServiceImpl;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @FunctionalInterface
    interface CategoryGenreStream<T>{
        Supplier<T> getNextItem();
    }

// I am assuming that this factory encapsulates iteration state
   CategoryGenreStream<Category> categoryFactory = () ->{
        System.out.println("categoryFactory was Created");
        return categoryServiceImpl.CategoryFactory();
   };
// this is an interface object method which returns a supplier method that when call returns data
    // Object Method that creates object-> Supplier Method -> Data
    CategoryGenreStream<Map<Category, List<Genre>>> genrePairCategoryFactory
        (CategoryGenreStream<Category> categoryFactory) {
        // unpack passed behavior
        Supplier<Category> categoryWorker = categoryFactory.getNextItem();
        System.out.println("category Worker was created inside Pair factory");

        return () -> () -> {
            Category category = categoryWorker.get();
            if(category==null){
                return null;
            }
            Map<Category, List<Genre>> pair = new HashMap<>();
            pair.put(category, genreServiceImpl.findAllByCategory(category));
            return pair;
        };
    }

    CategoryGenreStream<HashMap<Category, List<Genre>>> streamGenrePairsIntoMap(
            CategoryGenreStream<Map<Category,List<Genre>>> pairFactory) {

        //Unpack pairFactory
        Supplier<Map<Category, List<Genre>>> pairWorker=  pairFactory.getNextItem();
        // pass HashMap
        HashMap<Category, List<Genre>> categoryGenreMap = new HashMap<>();

        return () -> ()->{

            Map<Category, List<Genre>> pair = pairWorker.get();

                if(pair==null) {
                    System.out.println("PairFactory is null");
                    return null;
                }
                categoryGenreMap.putAll(pair);
                return categoryGenreMap;
        };
    }

    // Genre gallery page (categories + genres)
    @GetMapping
    public String showGenres(Model model) {

        Supplier<HashMap<Category,List<Genre>>> streamGenrePairWorker =
                streamGenrePairsIntoMap(genrePairCategoryFactory(categoryFactory)).getNextItem();

        HashMap<Category,List<Genre>> categoryGenreMap = new HashMap<>();

        HashMap<Category,List<Genre>> next;

        while((next = streamGenrePairWorker.get()) != null){
            categoryGenreMap = next;
        }

        System.out.println(categoryGenreMap);
        System.out.println("categoryGenreMap:"+categoryGenreMap);

        model.addAttribute("categoryGenreMap", categoryGenreMap);
        model.addAttribute("categories", categoryServiceImpl.findAllCategories());
        model.addAttribute("view", "genres");
        return "main";
    }

    // Add genre (ADMIN only — enforced in SecurityConfig)
    @PostMapping("/add")
    public String addGenre(@ModelAttribute Genre genre) {
        genreServiceImpl.saveGenre(genre);
        return "redirect:/genre";
    }

    @ModelAttribute("genre")
    public Genre genre() {
        return new Genre();
    }

    // Individual genre page with poems and essays
    @GetMapping("/{id}")
    public String loadGenrePage(@PathVariable Long id, Model model){
        Genre genre = genreServiceImpl.findGenreById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        List<Poem> poems = poemServiceImpl.getPoemsByGenre(genre);
        List<Essay> essays = essayServiceImpl.findByGenre(genre);

        model.addAttribute("genre", genre);
        model.addAttribute("poems", poems);
        model.addAttribute("essays", essays);

        model.addAttribute("view", "genreTemplate");
        return "main";
    }
}
