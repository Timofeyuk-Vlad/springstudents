package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.LogTaskStatusDto;
import java.time.LocalDate;
import java.util.Optional;

public interface AsyncLogGenerationService {

  /**
   * Инициирует асинхронную генерацию лог-файла за указанную дату.
   * @param date Дата, за которую нужно сгенерировать лог.
   * @return Уникальный ID созданной задачи.
   */
  String generateLogFileAsync(LocalDate date);

  /**
   * Получает статус задачи по генерации лог-файла.
   * @param taskId ID задачи.
   * @return Optional с DTO статуса задачи или Optional.empty(), если задача не найдена.
   */
  Optional<LogTaskStatusDto> getTaskStatus(String taskId);

  /**
   * Получает путь к сгенерированному лог-файлу, если задача завершена успешно.
   * @param taskId ID задачи.
   * @return Optional с путем к файлу или Optional.empty(), если файл не готов или задача не найдена/провалилась.
   */
  Optional<String> getGeneratedLogFilePath(String taskId);
}