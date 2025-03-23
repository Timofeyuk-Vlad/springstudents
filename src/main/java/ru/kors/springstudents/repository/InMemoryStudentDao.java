package ru.kors.springstudents.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

@Repository
public class InMemoryStudentDao {
    private final List<Student> students = new ArrayList<>();

    public List<Student> findAllStudent() {
        return students;
    }

    public Student saveStudent(Student student) {
        var studentIndex = IntStream.range(0, students.size())
            .filter(index -> students.get(index).getId().equals(student.getId()))
            .findFirst()
            .orElse(-1);
        if (studentIndex > -1) {
            return null; //false
        }
        students.add(student);
        //return true;
        return student;
    }

    public Student findByEmail(String email) {
        return students.stream()
    .filter(element -> element.getEmail().equals(email))
    .findFirst()
    .orElse(null);
    }

    public Optional<Student> findById(Long id) {
        return students.stream()
            .filter(element -> element.getId().equals(id))
            .findFirst();
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

    public void deleteStudent(Long id) {
        Student student = findById(id)
            .orElseThrow(() -> new RuntimeException("Студент с id " + id + " не найден"));
        students.remove(student);
    }
}
