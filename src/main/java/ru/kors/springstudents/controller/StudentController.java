package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kors.springstudents.dto.*;
import ru.kors.springstudents.service.StudentService;

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
}