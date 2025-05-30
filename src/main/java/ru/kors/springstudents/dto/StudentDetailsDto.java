package ru.kors.springstudents.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class StudentDetailsDto {
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private int age;
  private Set<EventDto> events;
  private Set<BarterDto> barters;
}