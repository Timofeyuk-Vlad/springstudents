package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.LogTaskStatusDto;
import java.time.LocalDate;
import java.util.Optional;

public interface AsyncLogGenerationService {

  String generateLogFileAsync(LocalDate date);

  /**
   * Асинхронно обрабатывает генерацию лог-файла.
   * Этот метод должен быть public и помечен @Async в реализации.
   * @param taskId ID задачи.
   * @param date Дата, за которую генерируется лог.
   */
  void processLogGeneration(String taskId, LocalDate date); // <--- ДОБАВЬ ЭТОТ МЕТОД

  Optional<LogTaskStatusDto> getTaskStatus(String taskId);

  Optional<String> getGeneratedLogFilePath(String taskId);
}