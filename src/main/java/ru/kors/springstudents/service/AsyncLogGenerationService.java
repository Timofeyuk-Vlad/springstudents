package ru.kors.springstudents.service;

import java.time.LocalDate;
import java.util.Optional;
import ru.kors.springstudents.dto.LogTaskStatusDto;

public interface AsyncLogGenerationService {

  String generateLogFileAsync(LocalDate date);

  void processLogGeneration(String taskId, LocalDate date); // <--- ДОБАВЬ ЭТОТ МЕТОД

  Optional<LogTaskStatusDto> getTaskStatus(String taskId);

  Optional<String> getGeneratedLogFilePath(String taskId);
}