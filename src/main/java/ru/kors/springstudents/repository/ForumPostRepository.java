package ru.kors.springstudents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springstudents.model.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
}