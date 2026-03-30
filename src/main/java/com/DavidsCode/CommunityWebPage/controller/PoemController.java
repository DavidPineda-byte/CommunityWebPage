package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Genre;
import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.repository.poemGenreRepository;
import com.DavidsCode.CommunityWebPage.service.GenreServiceImpl;
import com.DavidsCode.CommunityWebPage.service.UserService;
import com.DavidsCode.CommunityWebPage.service.poemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/poem")
public class PoemController {
    @Autowired
    private poemServiceImpl poemServiceImpl;
    @Autowired
    private GenreServiceImpl genreServiceImpl;

    @GetMapping
    public String showPoemGenres(Model model) {
        List<Genre> genres = genreServiceImpl.findAllGenres();
        model.addAttribute("genres", genres);
        return "PoemGenreMenu";
    }

    @PostMapping("/addGenre")
    public String addGenre(@ModelAttribute Genre genre) {
        genreServiceImpl.saveGenre(genre);
        return "redirect:/poem";
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
    @ModelAttribute("genre")
    public Genre genre() {
        return new Genre();
    }

    @GetMapping("/genres/{id}")
    public String LoadGenrePoem(@PathVariable Long id, Model model){
        Genre genre = genreServiceImpl.findGenreById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        List<Poem> poems = poemServiceImpl.poemRepository.findByGenre(genre.getName());

        model.addAttribute("poems", poems);
        return "displayPoems";
    }
}
