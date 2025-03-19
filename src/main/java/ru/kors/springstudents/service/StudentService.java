package ru.kors.springstudents.service;

import java.util.List;
import ru.kors.springstudents.model.Student;

public interface StudentService {
    List<Student> findAllStudent();

    boolean saveStudent(Student student);

    Student findByEmail(String email);

    Student findById(Integer id);

    Student updateStudent(Student student);

    boolean deleteStudent(Integer id);
}
