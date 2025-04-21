package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentSummaryDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private int age;
}