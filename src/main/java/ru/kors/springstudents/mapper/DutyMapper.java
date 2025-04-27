package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateDutyRequestDto;
import ru.kors.springstudents.dto.DutyDto;
import ru.kors.springstudents.model.Duty;

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface DutyMapper {
    @Mapping(source = "student.id", target = "studentId")
        // @Mapping(source = "student", target = "studentFullName", qualifiedByName = "getStudentFullName") // <-- УБИРАЕМ ЭТУ СТРОКУ
    DutyDto toDto(Duty duty);

    List<DutyDto> toDtoList(List<Duty> duties);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Duty toEntity(CreateDutyRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateDutyRequestDto dto, @MappingTarget Duty duty);
}