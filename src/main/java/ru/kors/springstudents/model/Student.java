package ru.kors.springstudents.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class Student {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private int age;
}
