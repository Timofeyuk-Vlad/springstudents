package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public ResponseEntity<List<StudentDto>> findAllStudents() {
        return ResponseEntity.ok(service.findAllStudent());
    }

    @PostMapping("/save_student")
    public ResponseEntity<StudentDto> saveStudent(
        @Valid @RequestBody CreateStudentRequestDto studentRequest) {
        StudentDto savedStudent = service.saveStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findDtoByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDtoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(
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