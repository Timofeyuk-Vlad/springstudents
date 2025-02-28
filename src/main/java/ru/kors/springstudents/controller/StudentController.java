package ru.kors.springstudents.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    service.saveStudent(student);
    return "Student successfully saved";
  }

  @GetMapping("/email/{email}")
  public Student findByEmail(@PathVariable String email) {
    return service.findByEmail(email);
  }

  @GetMapping("/id/{id}")
  public Student findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @GetMapping("/search")
  public Student findStudent(@RequestParam(required = false) String email,
                 @RequestParam(required = false) Integer id) {
    if (email != null) {
      return service.findByEmail(email);
    } else if (id != null) {
      return service.findById(id);
    }
    return null;
  }

  @PutMapping("update_student")
  public Student updateStudent(@RequestBody Student student) {
    return service.updateStudent(student);
  }

  @DeleteMapping("delete_student/{email}")
  public void deleteStudent(@PathVariable String email) {
    service.deleteStudent(email);
  }
}
