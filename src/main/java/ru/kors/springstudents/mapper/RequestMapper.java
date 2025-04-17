package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateRequestDto;
import ru.kors.springstudents.dto.RequestDto;
import ru.kors.springstudents.model.Request;
import ru.kors.springstudents.model.Student;

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface RequestMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student", target = "studentFullName", qualifiedByName = "getStudentFullName")
    RequestDto toDto(Request request);

    List<RequestDto> toDtoList(List<Request> requests);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // Обычно устанавливается при сохранении
    @Mapping(target = "student", ignore = true)
    Request toEntity(CreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateRequestDto dto, @MappingTarget Request request); // Или UpdateDTO
}