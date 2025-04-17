package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kors.springstudents.dto.CreateForumPostRequestDto;
import ru.kors.springstudents.dto.ForumPostDto;
import ru.kors.springstudents.service.ForumPostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum-posts") // Согласованное имя пути
@RequiredArgsConstructor
public class ForumPostController {

    private final ForumPostService service;

    @GetMapping
    public ResponseEntity<List<ForumPostDto>> findAllForumPosts() {
        return ResponseEntity.ok(service.findAllForumPosts());
    }

    @PostMapping
    public ResponseEntity<ForumPostDto> saveForumPost(@Valid @RequestBody
                                                          CreateForumPostRequestDto postDto) {
        ForumPostDto savedPost = service.saveForumPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumPostDto> findForumPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findForumPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ForumPostDto> updateForumPost(@PathVariable Long id,
                                                        @Valid @RequestBody
                                                        CreateForumPostRequestDto postDto) {
        return ResponseEntity.ok(service.updateForumPost(id, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForumPost(@PathVariable Long id) {
        service.deleteForumPost(id);
        return ResponseEntity.noContent().build();
    }
}