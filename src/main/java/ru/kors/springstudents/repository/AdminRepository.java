package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}