package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DutyDto {
    private Long id;
    private LocalDate date;
    private Long studentId;
    private String studentFullName;
}