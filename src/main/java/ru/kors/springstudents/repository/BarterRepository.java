package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.Barter;

public interface BarterRepository extends JpaRepository<Barter, Long> {
}