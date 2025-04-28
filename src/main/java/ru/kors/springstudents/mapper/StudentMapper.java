package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.*;
import ru.kors.springstudents.model.Student;

import java.util.List;
import java.util.Set; // Импортируем Set для ясности (хотя MapStruct разберется)

@Mapper(componentModel = "spring", uses = {
    HelperMapper.class,
    RequestMapper.class,
    EventMapper.class,
    DutyMapper.class,
    ForumPostMapper.class,
    BarterMapper.class
})
public interface StudentMapper {

    // Для краткой сводки
    StudentSummaryDto toSummaryDto(Student student);
    List<StudentSummaryDto> toSummaryDtoList(List<Student> students); // Принимаем List<Student> из репозитория

    // Для полной информации (MapStruct смаппит Set<Entity> в Set<Dto>)
    StudentDetailsDto toDetailsDto(Student student);
    List<StudentDetailsDto> toDetailsDtoList(List<Student> students); // Принимаем List<Student> из репозитория

    // Для создания студента
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    Student toEntity(CreateStudentRequestDto dto);

    // Для обновления студента
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    void updateEntityFromDto(UpdateStudentRequestDto dto, @MappingTarget Student student);
}