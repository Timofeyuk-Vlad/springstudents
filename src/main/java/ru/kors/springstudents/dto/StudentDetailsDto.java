package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class StudentDetailsDto { // Новое DTO для деталей
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private int age;
  // --- Списки вложенных DTO ---
  private Set<RequestDto> requests;
  private Set<EventDto> events;
  private Set<DutyDto> duties;
  private Set<ForumPostDto> forumPosts;
  private Set<BarterDto> barters;
}