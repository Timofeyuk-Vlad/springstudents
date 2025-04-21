package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.EntityGraph; // Для EntityGraph
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

import java.util.List;
import java.util.Optional; // Optional нужен для findById

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByEmail(@NonNull String email); // Оставляем для проверок

    // Используем EntityGraph для загрузки всех связей при поиске по ID
    @Override
    @EntityGraph(attributePaths = {"requests", "events", "duties", "forumPosts", "barters"})
    Optional<Student> findById(@NonNull Long id);

    // Метод для получения краткого списка (без JOIN FETCH)
    // Стандартный findAll() подойдет, или можно сделать проекцию, если нужно оптимизировать
    // List<Student> findAll();
}