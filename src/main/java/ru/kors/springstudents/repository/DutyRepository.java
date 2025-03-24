package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.Duty;

public interface DutyRepository extends JpaRepository<Duty, Long> {
}