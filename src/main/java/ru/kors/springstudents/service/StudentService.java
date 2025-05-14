package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;

import java.util.List;

public interface StudentService {

    /**
     * Получает краткую сводку по всем студентам.
     * @return Список StudentSummaryDto.
     */
    List<StudentSummaryDto> findAllStudentsSummary();

    /**
     * Получает полную информацию по всем студентам (включая детали связей).
     * @return Список StudentDetailsDto.
     */
    List<StudentDetailsDto> findAllStudentsDetails();

    /**
     * Находит полную информацию о студенте по ID. Использует кэш.
     * @param id ID студента.
     * @return StudentDetailsDto.
     * @throws ru.kors.springstudents.exception.ResourceNotFoundException если студент не найден.
     */
    StudentDetailsDto findStudentDetailsById(Long id);

    /**
     * Сохраняет нового студента.
     * @param studentDto DTO с данными для создания.
     * @return DTO с деталями сохраненного студента.
     */
    StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto);

    /**
     * Находит полную информацию о студенте по email. Использует кэш (через findById).
     * @param email Email студента.
     * @return StudentDetailsDto.
     * @throws ru.kors.springstudents.exception.ResourceNotFoundException если студент не найден.
     */
    StudentDetailsDto findDtoByEmail(String email);

    /**
     * Обновляет существующего студента. Инвалидирует кэш.
     * @param id ID студента для обновления.
     * @param studentDto DTO с новыми данными.
     * @return DTO с деталями обновленного студента.
     * @throws ru.kors.springstudents.exception.ResourceNotFoundException если студент не найден.
     */
    StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto);

    /**
     * Удаляет студента по ID. Инвалидирует кэш.
     * @param id ID студента для удаления.
     * @throws ru.kors.springstudents.exception.ResourceNotFoundException если студент не найден.
     */
    void deleteStudent(Long id);

    /**
     * Находит студентов (краткая информация), участвующих в событии с заданным именем (JPQL).
     * @param eventName Имя события.
     * @return Список StudentSummaryDto.
     */
    List<StudentSummaryDto> findStudentsByEventName(String eventName);

    /**
     * Находит студентов (краткая информация), имеющих активный обмен по заданному предмету (Native Query).
     * @param itemName Название предмета обмена.
     * @return Список StudentSummaryDto.
     */
    List<StudentSummaryDto> findStudentsWithActiveBarterByItem(String itemName);

    /**
     * Массово сохраняет список новых студентов.
     * @param studentDtos Список DTO с данными для создания.
     * @return Список DTO с деталями сохраненных студентов.
     * @throws IllegalArgumentException если email одного из студентов уже существует.
     */
    List<StudentDetailsDto> saveStudentsBulk(List<CreateStudentRequestDto> studentDtos);
}