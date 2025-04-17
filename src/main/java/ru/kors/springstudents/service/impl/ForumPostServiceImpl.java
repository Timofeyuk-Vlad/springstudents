package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateForumPostRequestDto;
import ru.kors.springstudents.dto.ForumPostDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.ForumPostMapper;
import ru.kors.springstudents.model.ForumPost;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.ForumPostRepository;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.ForumPostService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ForumPostServiceImpl implements ForumPostService {

    private final ForumPostRepository repository;
    private final StudentRepository studentRepository;
    private final ForumPostMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ForumPostDto> findAllForumPosts() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public ForumPostDto saveForumPost(CreateForumPostRequestDto postDto) {
        Student author = studentRepository.findById(postDto.getAuthorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author (Student) with id "
                + postDto.getAuthorId() + " not found for ForumPost"));

        ForumPost post = mapper.toEntity(postDto);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        ForumPost savedPost = repository.save(post);
        return mapper.toDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public ForumPostDto findForumPostById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("ForumPost with id "
                + id + " not found"));
    }

    @Override
    public ForumPostDto updateForumPost(Long id, CreateForumPostRequestDto postDto) {
        ForumPost existingPost = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ForumPost with id "
                + id + " not found"));

        // Проверяем смену автора (если разрешено)
        if (!existingPost.getAuthor().getId().equals(postDto.getAuthorId())) {
            Student newAuthor = studentRepository.findById(postDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("New Author (Student) with id "
                    + postDto.getAuthorId() + " not found for ForumPost update"));
            existingPost.setAuthor(newAuthor);
        }

        mapper.updateEntityFromDto(postDto, existingPost);

        ForumPost updatedPost = repository.save(existingPost);
        return mapper.toDto(updatedPost);
    }

    @Override
    public void deleteForumPost(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ForumPost with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}