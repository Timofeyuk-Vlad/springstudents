package ru.kors.springstudents.controller;

import java.util.List;
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

@SuppressWarnings("checkstyle:Indentation")
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
  public String saveStudent(@RequestBody Student student) {
    boolean isSaved = service.saveStudent(student);
    if (!isSaved) {
      return "There is already a student with ID " + student.getId();
    }
    return "Student with id " + student.getId() + " successfully saved";
  }

  @GetMapping("/email/{email}")
  public Student findByEmail(@PathVariable String email) {
    Student student = service.findByEmail(email);
    if (student == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with email "
          + email + " not found");
    }
    return student;
  }

  @GetMapping("/{id}")
  public Student findById(@PathVariable Integer id) {
    Student student = service.findById(id);
    if (student == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id
          + " not found");
    }
    return student;
  }

  @GetMapping("/search")
  public Student findStudent(@RequestParam(required = false) String email) {
    if (email != null) {
      Student student = service.findByEmail(email);
      if (student == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with email " + email
            + " not found");
      }
      return student;
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "Either email or id must be provided");
  }

  @PutMapping("update_student")
  public Student updateStudent(@RequestBody Student student) {
    Student updatedStudent = service.updateStudent(student);
    if (updatedStudent == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
    }
    return updatedStudent;
  }

  @DeleteMapping("delete_student/{id}")
  public String deleteStudent(@PathVariable Integer id) {
    boolean isDeleted = service.deleteStudent(id);
    if (!isDeleted) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id "
          + id + " not found");
    }
    return "Student with id " + id + " successfully delete";
  }
}
