package ru.kors.springstudents.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateDutyRequestDto {
    @NotNull
    @FutureOrPresent // Пример валидации даты
    private LocalDate date;
    @NotNull
    private Long studentId;
}