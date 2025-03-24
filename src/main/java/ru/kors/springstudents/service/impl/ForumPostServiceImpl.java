package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.ForumPost;
import ru.kors.springstudents.repository.ForumPostRepository;
import ru.kors.springstudents.service.ForumPostService;

import java.util.List;

@Service
@AllArgsConstructor
public class ForumPostServiceImpl implements ForumPostService {
    private final ForumPostRepository repository;

    @Override
    public List<ForumPost> findAllForumPosts() {
        return repository.findAll();
    }

    @Override
    public ForumPost saveForumPost(ForumPost forumPost) {
        return repository.save(forumPost);
    }

    @Override
    public ForumPost findForumPostById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ForumPost updateForumPost(ForumPost forumPost) {
        return repository.save(forumPost);
    }

    @Override
    public void deleteForumPost(Long id) {
        repository.deleteById(id);
    }
}