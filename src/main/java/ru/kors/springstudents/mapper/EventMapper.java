package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.model.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface EventMapper {

    @Mapping(target = "studentIds", source = "students", qualifiedByName = "studentsToStudentIds")
    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    Event toEntity(CreateEventRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateEntityFromDto(CreateEventRequestDto dto,
                             @MappingTarget Event event);
}