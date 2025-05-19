package ru.kors.springstudents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBarterRequestDto {
    @NotBlank
    private String item;
    private String description;
    @NotBlank
    private String status;
    @NotNull
    private Long studentId; // ID студента, который создает
}