package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateForumPostRequestDto; // camelCase
import ru.kors.springstudents.dto.ForumPostDto;             // camelCase
import ru.kors.springstudents.model.ForumPost;
// import ru.kors.springstudents.model.Student;

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface ForumPostMapper {
    @Mapping(source = "author.id", target = "authorId")
        // @Mapping(source = "author", target = "authorFullName", qualifiedByName = "getStudentFullName") // <-- УБИРАЕМ ЭТУ СТРОКУ
    ForumPostDto toDto(ForumPost post);

    List<ForumPostDto> toDtoList(List<ForumPost> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    ForumPost toEntity(CreateForumPostRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntityFromDto(CreateForumPostRequestDto dto, @MappingTarget ForumPost post);
}