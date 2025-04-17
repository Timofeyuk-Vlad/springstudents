package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.model.Student;

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface StudentMapper {

    @Mapping(target = "requestIds", source = "requests", qualifiedByName = "requestsToRequestIds")
    @Mapping(target = "eventIds", source = "events", qualifiedByName = "eventsToEventIds")
    @Mapping(target = "dutyIds", source = "duties", qualifiedByName = "dutiesToDutyIds")
    @Mapping(target = "forumPostIds", source = "forumPosts",
        qualifiedByName = "forumPostsToForumPostIds")
    @Mapping(target = "barterIds", source = "barters", qualifiedByName = "bartersToBarterIds")
    StudentDto toDto(Student student);

    List<StudentDto> toDtoList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    Student toEntity(CreateStudentRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    void updateEntityFromDto(UpdateStudentRequestDto dto, @MappingTarget Student student);
}