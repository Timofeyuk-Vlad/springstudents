package ru.kors.springstudents.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.kors.springstudents.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByEmail(@NonNull String email);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"events", "barters"})
    Optional<Student> findById(@NonNull Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"events", "barters"})
    List<Student> findAll();

    @Query("SELECT DISTINCT s FROM Student s JOIN s.events e WHERE e.name = :eventName")
    List<Student> findStudentsByEventNameJpql(@Param("eventName") String eventName);

    @Query(value = "SELECT s.* FROM students s JOIN barters b ON s.id = b.student_id "
        + "WHERE b.item = :itemName", nativeQuery = true)
    List<Student> findStudentsWithActiveBarterByItemNative(@Param("itemName") String itemName);

}