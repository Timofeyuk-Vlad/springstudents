package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateForumPostRequestDto;
import ru.kors.springstudents.dto.ForumPostDto;
// import ru.kors.springstudents.dto.UpdateForumPostRequestDTO;

import java.util.List;

public interface ForumPostService {
    List<ForumPostDto> findAllForumPosts();
    
    ForumPostDto saveForumPost(CreateForumPostRequestDto postDto);
    
    ForumPostDto findForumPostById(Long id);
    
    ForumPostDto updateForumPost(Long id, CreateForumPostRequestDto postDto);
    
    void deleteForumPost(Long id);
    
}