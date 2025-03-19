package ru.kors.springstudents.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

@Repository
public class InMemoryStudentDao {
    private final List<Student> students = new ArrayList<>();

    public List<Student> findAllStudent() {
        return students;
    }

    public boolean saveStudent(Student student) {
        var studentIndex = IntStream.range(0, students.size())
            .filter(index -> students.get(index).getId().equals(student.getId()))
            .findFirst()
            .orElse(-1);
        if (studentIndex > -1) {
            return false;
        }
        students.add(student);
        return true;
    }

    public Student findByEmail(String email) {
        return students.stream()
    .filter(element -> element.getEmail().equals(email))
    .findFirst()
    .orElse(null);
    }

    public Student findById(Integer id) {
        return students.stream()
    .filter(element -> element.getId().equals(id))
    .findFirst()
    .orElse(null);
    }

    public Student updateStudent(Student student) {
        var studentIndex = IntStream.range(0, students.size())
                .filter(index -> students.get(index).getId().equals(student.getId()))
                .findFirst()
                .orElse(-1);
        if (studentIndex > -1) {
            students.set(studentIndex, student);
            return student;
        }
        return null;
    }

    public boolean deleteStudent(Integer id) {
        var student = findById(id);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }
}
