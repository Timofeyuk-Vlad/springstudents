package ru.kors.springstudents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogTaskStatusDto {
  private String taskId;
  private TaskStatus status;
  private String message;
  private String filePath;

  public enum TaskStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
  }
}