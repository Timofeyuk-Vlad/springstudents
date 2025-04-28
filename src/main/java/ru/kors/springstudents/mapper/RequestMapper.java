package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateRequestDto;
import ru.kors.springstudents.dto.RequestDto;
import ru.kors.springstudents.model.Request;

import java.util.List;
import java.util.Set; // Импорт Set

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface RequestMapper {

    @Mapping(source = "student.id", target = "studentId")
    RequestDto toDto(Request request);

    // Принимаем Set<Request>, возвращаем List<RequestDto>
    // MapStruct сможет использовать это для маппинга в Set<RequestDto> в StudentMapper
    List<RequestDto> toDtoList(Set<Request> requests);

    // Маппинг для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "student", ignore = true)
    Request toEntity(CreateRequestDto dto);

    // Маппинг для обновления (если нужен)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateRequestDto dto, @MappingTarget Request request);
}