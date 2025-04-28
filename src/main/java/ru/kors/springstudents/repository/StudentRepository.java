package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull; // Используем NonNull из Spring
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Находит студента по email (не загружает связи, используется в проверках)
    Student findStudentByEmail(@NonNull String email);

    // Переопределяем findById, чтобы он загружал все связи
    @Override
    @NonNull // Аннотация Spring для указания non-null
    @EntityGraph(attributePaths = {"requests", "events", "duties", "forumPosts", "barters"})
    Optional<Student> findById(@NonNull Long id);

    // Переопределяем findAll, чтобы он загружал все связи для каждого студента
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"requests", "events", "duties", "forumPosts", "barters"})
    List<Student> findAll();
}