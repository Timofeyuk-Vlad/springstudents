package ru.kors.springstudents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBarterRequestDto {
    @NotBlank
    private String item;
    private String description;
    @NotBlank
    private String status;
    @NotNull
    private Long studentId; // ID студента, который создает
}