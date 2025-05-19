package ru.kors.springstudents.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentSummaryDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private int age;
}