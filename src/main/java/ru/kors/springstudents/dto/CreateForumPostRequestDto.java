package ru.kors.springstudents.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateForumPostRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Long authorId;
}