package ru.kors.springstudents.service;

import java.util.List;
import java.util.Optional;

import ru.kors.springstudents.model.Student;

public interface StudentService {
    List<Student> findAllStudent();

    Student saveStudent(Student student);

    Student findByEmail(String email);

    Optional<Student> findById(Long id);

    Student updateStudent(Student student);

    void deleteStudent(Long id);
}
