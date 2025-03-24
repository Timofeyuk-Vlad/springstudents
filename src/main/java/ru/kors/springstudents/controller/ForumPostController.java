package ru.kors.springstudents.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.model.ForumPost;
import ru.kors.springstudents.service.ForumPostService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum-posts")
@AllArgsConstructor
public class ForumPostController {
    private final ForumPostService service;

    @GetMapping
    public List<ForumPost> findAllForumPosts() {
        return service.findAllForumPosts();
    }

    @PostMapping
    public ForumPost saveForumPost(@RequestBody ForumPost forumPost) {
        return service.saveForumPost(forumPost);
    }

    @GetMapping("/{id}")
    public ForumPost findForumPostById(@PathVariable Long id) {
        return service.findForumPostById(id);
    }

    @PutMapping
    public ForumPost updateForumPost(@RequestBody ForumPost forumPost) {
        return service.updateForumPost(forumPost);
    }

    @DeleteMapping("/{id}")
    public void deleteForumPost(@PathVariable Long id) {
        service.deleteForumPost(id);
    }
}