package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto; // Детали
import ru.kors.springstudents.dto.StudentSummaryDto; // Сводка
import ru.kors.springstudents.dto.UpdateStudentRequestDto;

import java.util.List;

public interface StudentService {
    List<StudentSummaryDto> findAllStudentsSummary(); // Метод для списка

    StudentDetailsDto findStudentDetailsById(Long id); // Метод для деталей

    StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto); // Возвращаем детали после создания

    StudentDetailsDto findDtoByEmail(String email); // Возможно, тоже детали? Или Summary? Реши сам.

    StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto); // Возвращаем детали после обновления

    void deleteStudent(Long id);
}