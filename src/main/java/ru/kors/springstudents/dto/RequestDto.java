package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private String type;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private Long studentId;
    private String studentFullName;
}