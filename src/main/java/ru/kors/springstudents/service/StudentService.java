package ru.kors.springstudents.service;

import java.util.List;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;

public interface StudentService {

    List<StudentSummaryDto> findAllStudentsSummary();

    List<StudentDetailsDto> findAllStudentsDetails();

    StudentDetailsDto findStudentDetailsById(Long id);

    StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto);

    StudentDetailsDto findDtoByEmail(String email);

    StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto);

    void deleteStudent(Long id);

    List<StudentSummaryDto> findStudentsByEventName(String eventName);

    List<StudentSummaryDto> findStudentsWithActiveBarterByItem(String itemName);

    List<StudentDetailsDto> saveStudentsBulk(List<CreateStudentRequestDto> studentDtos);
}