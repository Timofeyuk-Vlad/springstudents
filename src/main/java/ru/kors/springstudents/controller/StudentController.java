package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kors.springstudents.dto.*;
import ru.kors.springstudents.service.StudentService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public ResponseEntity<List<StudentSummaryDto>> findAllStudentsSummary() {
        return ResponseEntity.ok(service.findAllStudentsSummary());
    }

    @GetMapping("/details")
    public ResponseEntity<List<StudentDetailsDto>> findAllStudentsDetails() {
        List<StudentDetailsDto> studentsDetails = service.findAllStudentsDetails();
        return ResponseEntity.ok(studentsDetails);
    }

    @PostMapping
    public ResponseEntity<StudentDetailsDto> saveStudent(
        @Valid @RequestBody CreateStudentRequestDto studentRequest) {
        StudentDetailsDto savedStudent = service.saveStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDetailsDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findDtoByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findStudentDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> updateStudent(
        @PathVariable Long id,
        @Valid @RequestBody UpdateStudentRequestDto studentRequest) {
        return ResponseEntity.ok(service.updateStudent(id, studentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // --- НОВЫЕ ЭНДПОИНТЫ для кастомных запросов ---

    /**
     * Находит студентов (краткая информация), участвующих в событии с заданным именем.
     * Использует JPQL запрос.
     * Пример запроса: GET /api/v1/students/search/by-event-name?name=Конференция
     *
     * @param eventName Имя события (обязательный параметр запроса).
     * @return Список StudentSummaryDto или пустой список.
     */
    @GetMapping("/search/by-event-name")
    public ResponseEntity<List<StudentSummaryDto>> findStudentsByEventName(
        @RequestParam(name = "name") String eventName) {
        return ResponseEntity.ok(service.findStudentsByEventName(eventName));
    }

    /**
     * Находит студентов (краткая информация), имеющих активный обмен по заданному предмету.
     * Использует Native Query запрос.
     * Пример запроса: GET /api/v1/students/search/by-active-barter-item?item=Учебник по Java
     *
     * @param itemName Название предмета обмена (обязательный параметр запроса).
     * @return Список StudentSummaryDto или пустой список.
     */
    @GetMapping("/search/by-active-barter-item")
    public ResponseEntity<List<StudentSummaryDto>> findStudentsByActiveBarterItem(
        @RequestParam(name = "item") String itemName) {
        return ResponseEntity.ok(service.findStudentsWithActiveBarterByItem(itemName));
    }
}