package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;

import java.util.List;

public interface StudentService {
    List<StudentSummaryDto> findAllStudentsSummary();

    List<StudentDetailsDto> findAllStudentsDetails(); // Метод для получения всех деталей

    StudentDetailsDto findStudentDetailsById(Long id);

    StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto);

    StudentDetailsDto findDtoByEmail(String email);

    StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto);

    void deleteStudent(Long id);
}