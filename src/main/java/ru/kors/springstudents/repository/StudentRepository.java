package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    void deleteById(@NonNull Long id);

    Student findStudentByEmail(@NonNull String email);
}
