package ru.kors.springstudents.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CreateEventRequestDto {
    @NotBlank
    private String name;
    private String description;
    @NotNull @Future
    private LocalDateTime date;
    @Size(min = 1) // Хотя бы один участник
    private List<Long> studentIds; // ID студентов для добавления
}