package com.DavidsCode.CommunityWebPage.controller;

import com.DavidsCode.CommunityWebPage.entity.Poem;
import com.DavidsCode.CommunityWebPage.service.poemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/poem")
public class PoemRestController {

    @Autowired
   private poemServiceImpl poemServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<Poem> getPoemByTitle(@PathVariable Long id) {
        Poem poem = poemServiceImpl.getPoemById(id);
        return ResponseEntity.ok(poem);
    }

}
