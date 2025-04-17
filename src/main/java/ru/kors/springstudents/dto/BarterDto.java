package ru.kors.springstudents.dto;

import lombok.Data;

@Data
public class BarterDto {
    private Long id;
    private String item;
    private String description;
    private String status;
    private Long studentId; // ID студента вместо полного объекта
    private String studentFullName; // Можно добавить для удобства отображения
}