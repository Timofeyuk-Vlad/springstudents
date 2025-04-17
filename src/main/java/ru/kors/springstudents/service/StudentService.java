package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;

import java.util.List;

public interface StudentService {
    List<StudentDto> findAllStudent();

    StudentDto saveStudent(CreateStudentRequestDto studentDto);

    StudentDto findDtoByEmail(String email); // Бросает исключение, если не найден

    StudentDto findDtoById(Long id); // Бросает исключение, если не найден

    StudentDto updateStudent(Long id, UpdateStudentRequestDto studentDto); // Бросает исключение

    void deleteStudent(Long id); // Бросает исключение, если не найден

}