package ru.kors.springstudents.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private List<Long> studentIds;
}