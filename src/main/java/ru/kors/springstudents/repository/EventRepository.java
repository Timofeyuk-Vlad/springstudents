package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}