package ru.kors.springstudents.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private int age;
    // Списки ID связанных сущностей, чтобы избежать LazyInitializationException и циклов
    private List<Long> requestIds;
    private List<Long> eventIds;
    private List<Long> dutyIds;
    private List<Long> forumPostIds;
    private List<Long> barterIds;
}