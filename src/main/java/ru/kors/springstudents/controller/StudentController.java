package ru.kors.springstudents.controller;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public List<Student> findAllStudent() {
        return service.findAllStudent();
    }

    @PostMapping("save_student")
    public Student saveStudent(@RequestBody Student student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Студент не может быть null");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не может быть пустым");
        }

        Student existingStudent = service.findByEmail(student.getEmail());
        if (existingStudent != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Студент с email "
                + student.getEmail() + " уже существует");
        }
        return service.saveStudent(student);
    }

    @GetMapping("/email/{email}")
    public Student findByEmail(@PathVariable String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Email не может быть пустым");
        }
        Student student = service.findByEmail(email);
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Студент с email " + email + " не найден");
        }
        return student;
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Long id) {
        return service.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "Студент с id " + id + " не найден"));
    }

    @GetMapping("/search")
    public Student findStudent(@RequestParam(required = false) String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Email не может быть пустым");
        }
        Student student = service.findByEmail(email);
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Студент с email " + email + " не найден");
        }
        return student;
    }

    @PutMapping("update_student")
    public Student updateStudent(@RequestBody Student student) {
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Студент не может быть null");
        }
        if (student.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "ID студента не может быть null");
        }

        Optional<Student> existingStudent = service.findById(student.getId());
        if (existingStudent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Студент с id " + student.getId() + " не найден");
        }

        if (!existingStudent.get().getEmail().equals(student.getEmail())) {
            Student studentWithNewEmail = service.findByEmail(student.getEmail());
            if (studentWithNewEmail != null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Студент с email " + student.getEmail() + " уже существует");
            }
        }
        return service.updateStudent(student);
    }

    @DeleteMapping("delete_student/{id}")
    public void deleteStudent(@PathVariable Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID не может быть null");
        }

        Optional<Student> existingStudent = service.findById(id);
        if (existingStudent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Студент с id " + id + " не найден");
        }
        service.deleteStudent(id);
    }
}
