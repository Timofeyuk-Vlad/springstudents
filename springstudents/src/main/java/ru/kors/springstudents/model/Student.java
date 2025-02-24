package ru.kors.springstudents.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;



@Data
@Builder
public class Student {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @NonNull
    private String email;
    private int age;
}
