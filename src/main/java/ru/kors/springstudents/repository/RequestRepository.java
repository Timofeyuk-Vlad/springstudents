package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}