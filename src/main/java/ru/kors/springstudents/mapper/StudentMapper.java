package ru.kors.springstudents.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.model.Student;

@Mapper(componentModel = "spring", uses = {
    HelperMapper.class,
    EventMapper.class,
    BarterMapper.class
})
public interface StudentMapper {

    StudentSummaryDto toSummaryDto(Student student);

    List<StudentSummaryDto> toSummaryDtoList(List<Student> students);

    StudentDetailsDto toDetailsDto(Student student);

    List<StudentDetailsDto> toDetailsDtoList(List<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "barters", ignore = true)
    Student toEntity(CreateStudentRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "barters", ignore = true)
    void updateEntityFromDto(UpdateStudentRequestDto dto, @MappingTarget Student student);
}