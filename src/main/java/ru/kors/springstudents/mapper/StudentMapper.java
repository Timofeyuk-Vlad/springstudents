package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.*; // Импортируем все DTO
import ru.kors.springstudents.model.Student;

import java.util.List;

// Подключаем мапперы для вложенных DTO!
@Mapper(componentModel = "spring", uses = {HelperMapper.class, RequestMapper.class, EventMapper.class, DutyMapper.class, ForumPostMapper.class, BarterMapper.class})
public interface StudentMapper {

    // --- Маппинг для краткого списка ---
    StudentSummaryDto toSummaryDto(Student student);
    List<StudentSummaryDto> toSummaryDtoList(List<Student> students);

    // --- Маппинг для детального просмотра ---
    // MapStruct автоматически использует мапперы из uses для коллекций
    StudentDetailsDto toDetailsDto(Student student);
    // Метод List<StudentDetailsDto> не нужен, если детали получаем по одному

    // --- Маппинг из запросов на создание/обновление ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    Student toEntity(CreateStudentRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "duties", ignore = true)
    @Mapping(target = "forumPosts", ignore = true)
    @Mapping(target = "barters", ignore = true)
    void updateEntityFromDto(UpdateStudentRequestDto dto, @MappingTarget Student student);
}