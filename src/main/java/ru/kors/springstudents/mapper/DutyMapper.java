package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateDutyRequestDto;
import ru.kors.springstudents.dto.DutyDto;
import ru.kors.springstudents.model.Duty;

import java.util.List;
import java.util.Set; // Импорт Set

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface DutyMapper {

    @Mapping(source = "student.id", target = "studentId")
    DutyDto toDto(Duty duty);

    // Принимаем Set<Duty>
    List<DutyDto> toDtoList(Set<Duty> duties);

    // Маппинг для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Duty toEntity(CreateDutyRequestDto dto);

    // Маппинг для обновления (если нужен)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateDutyRequestDto dto, @MappingTarget Duty duty);
}