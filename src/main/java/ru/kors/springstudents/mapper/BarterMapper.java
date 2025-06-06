package ru.kors.springstudents.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.model.Barter;

@Mapper(componentModel = "spring", uses = HelperMapper.class)
public interface BarterMapper {

    @Mapping(source = "student.id", target = "studentId")
    BarterDto toDto(Barter barter);

    List<BarterDto> toDtoList(Set<Barter> barters);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Barter toEntity(CreateBarterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(CreateBarterRequestDto dto, @MappingTarget Barter barter);
}