package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDetailsDto { // Новое DTO для деталей
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String email;
  private int age;
  // --- Списки вложенных DTO ---
  private List<RequestDto> requests;
  private List<EventDto> events;
  private List<DutyDto> duties;
  private List<ForumPostDto> forumPosts;
  private List<BarterDto> barters;
}