package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.BarterDto; // Используем camelCase
import ru.kors.springstudents.dto.CreateBarterRequestDto; // Используем camelCase
import ru.kors.springstudents.model.Barter;
// import ru.kors.springstudents.model.Student; // Больше не нужен напрямую

import java.util.List;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface BarterMapper {
    @Mapping(source = "student.id", target = "studentId")
        // @Mapping(source = "student", target = "studentFullName", qualifiedByName = "getStudentFullName") // <-- УБИРАЕМ ЭТУ СТРОКУ
    BarterDto toDto(Barter barter);

    List<BarterDto> toDtoList(List<Barter> barters);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Barter toEntity(CreateBarterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateBarterRequestDto dto, @MappingTarget Barter barter);
}