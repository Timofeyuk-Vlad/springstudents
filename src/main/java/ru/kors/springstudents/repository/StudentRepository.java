package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByEmail(@NonNull String email);

    @Override
    @EntityGraph(attributePaths = {"requests", "events", "duties", "forumPosts", "barters"})
    Optional<Student> findById(@NonNull Long id);
}