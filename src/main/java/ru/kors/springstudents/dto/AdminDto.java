package ru.kors.springstudents.dto;

import lombok.Data;

@Data
public class AdminDto {
    private Long id;
    private String name;
    private String photoUrl;
    private String contactInfo;
}