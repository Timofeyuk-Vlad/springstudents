package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.model.Barter;

import java.util.List;
import java.util.Set; // Импорт Set

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface BarterMapper {

    @Mapping(source = "student.id", target = "studentId")
    BarterDto toDto(Barter barter);

    // Принимаем Set<Barter>
    List<BarterDto> toDtoList(Set<Barter> barters);

    // Маппинг для создания
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Barter toEntity(CreateBarterRequestDto dto);

    // Маппинг для обновления (если нужен)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateBarterRequestDto dto, @MappingTarget Barter barter);
}