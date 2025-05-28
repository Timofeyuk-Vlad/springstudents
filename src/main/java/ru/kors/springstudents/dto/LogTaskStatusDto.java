package ru.kors.springstudents.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogTaskStatusDto {
  private String taskId;
  private TaskStatus status;
  private String message; // Сообщение об ошибке или прогрессе
  private String filePath; // Путь к файлу, если готов

  public enum TaskStatus {
    PENDING,    // В очереди
    PROCESSING, // В обработке
    COMPLETED,  // Завершено
    FAILED      // Ошибка
  }
}