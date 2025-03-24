package ru.kors.springstudents.service;

import ru.kors.springstudents.model.ForumPost;

import java.util.List;

public interface ForumPostService {
    List<ForumPost> findAllForumPosts();

    ForumPost saveForumPost(ForumPost forumPost);

    ForumPost findForumPostById(Long id);

    ForumPost updateForumPost(ForumPost forumPost);

    void deleteForumPost(Long id);
}