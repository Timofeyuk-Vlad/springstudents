package ru.kors.springstudents.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationExceptionDetails {
    private String field;
    private String message;
}