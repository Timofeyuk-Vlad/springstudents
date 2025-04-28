package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.model.Event;

import java.util.List;
import java.util.Set; // Импорт Set

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface EventMapper {

    // Используем метод из HelperMapper, который теперь принимает Set<Student>
    @Mapping(target = "studentIds", source = "students", qualifiedByName = "studentsToStudentIds")
    EventDto toDto(Event event);

    // Принимаем Set<Event>
    List<EventDto> toDtoList(Set<Event> events);

    // Маппинг для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    Event toEntity(CreateEventRequestDto dto);

    // Маппинг для обновления (если нужен)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateEntityFromDto(CreateEventRequestDto dto, @MappingTarget Event event);
}