package ru.kors.springstudents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String error; // Код ошибки (e.g., "Not Found")
    private String message; // Описание ошибки
    private String path; // Путь запроса
    private List<String> details; // Дополнительные детали (например, ошибки валидации)
}