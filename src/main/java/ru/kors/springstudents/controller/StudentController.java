package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kors.springstudents.dto.*; // Импорт всех DTO
import ru.kors.springstudents.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping // Возвращает краткий список
    public ResponseEntity<List<StudentSummaryDto>> findAllStudents() {
        return ResponseEntity.ok(service.findAllStudentsSummary());
    }

    @PostMapping // Создает и возвращает детали
    public ResponseEntity<StudentDetailsDto> saveStudent(
        @Valid @RequestBody CreateStudentRequestDto studentRequest) {
        StudentDetailsDto savedStudent = service.saveStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping("/email/{email}") // Возвращает детали
    public ResponseEntity<StudentDetailsDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findDtoByEmail(email));
    }

    @GetMapping("/{id}") // Возвращает детали
    public ResponseEntity<StudentDetailsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findStudentDetailsById(id));
    }

    @PutMapping("/{id}") // Обновляет и возвращает детали
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
}