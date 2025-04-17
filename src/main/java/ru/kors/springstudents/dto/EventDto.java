package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private List<Long> studentIds; // Список ID студентов, участвующих в событии
}