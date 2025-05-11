package ru.kors.springstudents.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus; // <--- ДОБАВЬ ЭТОТ ИМПОРТ
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files; // Не используется, можно убрать
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; // Не используется, можно убрать
import java.util.stream.Collectors; // Не используется, можно убрать
import java.util.stream.Stream; // Не используется, можно убрать

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

  // Путь к папке с логами, можно вынести в application.yaml
  // В твоем logback-spring.xml это "logs/"
  @Value("${logging.file.path:logs}") // Значение по умолчанию 'logs'
  private String logsDirectoryPath;

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Возвращает содержимое лог-файла за указанную дату.
   * @param dateString Дата в формате YYYY-MM-DD.
   * @return Файл лога или 404, если файл не найден.
   */
  @GetMapping("/by-date")
  public ResponseEntity<InputStreamResource> getLogByDate(@RequestParam String dateString) {
    try {
      LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
      String logFileName = "application." + date.format(DATE_FORMATTER) + ".log";
      Path logFilePath = Paths.get(logsDirectoryPath, logFileName);

      File logFile = logFilePath.toFile();

      if (!logFile.exists() || !logFile.canRead()) {
        // Попробуем найти основной application.log, если лог за дату не найден и дата - сегодняшняя
        if (date.equals(LocalDate.now())) {
          logFilePath = Paths.get(logsDirectoryPath, "application.log");
          logFile = logFilePath.toFile();
          if (!logFile.exists() || !logFile.canRead()) {
            return ResponseEntity.notFound().build();
          }
        } else {
          return ResponseEntity.notFound().build();
        }
      }

      InputStreamResource resource = new InputStreamResource(new FileInputStream(logFile));

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + logFile.getName());
      headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
      headers.add(HttpHeaders.PRAGMA, "no-cache");
      headers.add(HttpHeaders.EXPIRES, "0");

      return ResponseEntity.ok()
          .headers(headers)
          .contentLength(logFile.length())
          .contentType(MediaType.TEXT_PLAIN) // или MediaType.APPLICATION_OCTET_STREAM
          .body(resource);

    } catch (java.time.format.DateTimeParseException e) {
      // Возвращаем 400, если формат даты неверный
      // Можно создать и вернуть ErrorResponseDto для более детальной ошибки
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      // Внутренняя ошибка сервера при чтении файла
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // HttpStatus теперь должен быть распознан
    }
  }
}