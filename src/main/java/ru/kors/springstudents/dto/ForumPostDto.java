package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Long authorId;
}