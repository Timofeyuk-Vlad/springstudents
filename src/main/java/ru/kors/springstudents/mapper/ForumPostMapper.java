package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateForumPostRequestDto;
import ru.kors.springstudents.dto.ForumPostDto;
import ru.kors.springstudents.model.ForumPost;

import java.util.List;
import java.util.Set; // Импорт Set

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface ForumPostMapper {

    @Mapping(source = "author.id", target = "authorId")
    ForumPostDto toDto(ForumPost post);

    // Принимаем Set<ForumPost>
    List<ForumPostDto> toDtoList(Set<ForumPost> posts);

    // Маппинг для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    ForumPost toEntity(CreateForumPostRequestDto dto);

    // Маппинг для обновления (если нужен)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntityFromDto(CreateForumPostRequestDto dto, @MappingTarget ForumPost post);
}