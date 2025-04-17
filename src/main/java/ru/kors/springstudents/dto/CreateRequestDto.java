package ru.kors.springstudents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRequestDto {
    @NotBlank
    private String type;
    @NotBlank
    private String description;
    @NotBlank
    private String status; // Или статус по умолчанию?
    @NotNull
    private Long studentId;
}