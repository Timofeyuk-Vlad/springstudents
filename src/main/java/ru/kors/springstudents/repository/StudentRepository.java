package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Добавить импорт
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

import java.util.List; // Добавить импорт

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByEmail(@NonNull String email);

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.requests")
    List<Student> findAllStudentsWithRequests();

    // Метод deleteById можно убрать, он есть в JpaRepository
    // void deleteById(@NonNull Long id);
}