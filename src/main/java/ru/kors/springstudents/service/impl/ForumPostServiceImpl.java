package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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

import java.time.LocalDateTime; // Импорт для установки времени
import java.util.HashSet; // Импорт HashSet
import java.util.List;
import java.util.Set;    // Импорт Set

@Service
@RequiredArgsConstructor
@Primary // Убери @Primary, если есть другой основной бин ForumPostService
public class ForumPostServiceImpl implements ForumPostService {

    private final ForumPostRepository repository;
    private final StudentRepository studentRepository;
    private final ForumPostMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ForumPostDto> findAllForumPosts() {
        List<ForumPost> postList = repository.findAll();
        // Преобразуем List в Set
        Set<ForumPost> postSet = new HashSet<>(postList);
        return mapper.toDtoList(postSet);
    }

    @Override
    @Transactional(readOnly = true)
    public ForumPostDto findForumPostById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("ForumPost not found with id: " + id));
    }

    @Override
    @Transactional
    public ForumPostDto saveForumPost(CreateForumPostRequestDto postDto) {
        Student author = studentRepository.findById(postDto.getAuthorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author (Student) not found with id: " + postDto.getAuthorId()));

        ForumPost post = mapper.toEntity(postDto);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now()); // Устанавливаем время создания
        ForumPost savedPost = repository.save(post);
        return mapper.toDto(savedPost);
    }

    @Override
    @Transactional
    public ForumPostDto updateForumPost(Long id, CreateForumPostRequestDto postDto) {
        ForumPost existingPost = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ForumPost not found with id: " + id));

        if (!existingPost.getAuthor().getId().equals(postDto.getAuthorId())) {
            Student newAuthor = studentRepository.findById(postDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author (Student) not found with id: " + postDto.getAuthorId()));
            existingPost.setAuthor(newAuthor);
        }

        mapper.updateEntityFromDto(postDto, existingPost); // Метод должен быть в ForumPostMapper
        // createdAt не обновляем при апдейте? Или обновляем? Реши сам.
        // existingPost.setCreatedAt(LocalDateTime.now());
        ForumPost updatedPost = repository.save(existingPost);
        return mapper.toDto(updatedPost);
    }

    @Override
    @Transactional
    public void deleteForumPost(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ForumPost not found with id: " + id);
        }
        repository.deleteById(id);
    }
}