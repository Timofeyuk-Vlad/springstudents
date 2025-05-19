package ru.kors.springstudents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarterDto {
    private Long id;
    private String item;
    private String description;
    private String status;
    private Long studentId;
}

