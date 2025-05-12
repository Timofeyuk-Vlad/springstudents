package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Tag(name = "Log API", description = "API для получения лог-файлов")
@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

  @Value("${logging.file.path:logs}")
  private String logsDirectoryPath;

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Operation(summary = "Получить лог-файл по дате",
      description = "Возвращает файл application.log за указанную дату. " +
          "Если запрошена сегодняшняя дата и ротированный файл не найден, вернет текущий application.log.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Лог-файл успешно получен",
          content = @Content(mediaType = "text/plain")),
      @ApiResponse(responseCode = "400", description = "Неверный формат даты (ожидается YYYY-MM-DD)"),
      @ApiResponse(responseCode = "404", description = "Лог-файл за указанную дату не найден"),
      @ApiResponse(responseCode = "500", description = "Ошибка при чтении файла лога")
  })
  @GetMapping("/by-date")
  public ResponseEntity<InputStreamResource> getLogByDate(
      @Parameter(description = "Дата в формате YYYY-MM-DD", required = true, example = "2025-05-05")
      @RequestParam String dateString) {
    try {
      LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
      String logFileName = "application." + date.format(DATE_FORMATTER) + ".log";
      Path logFilePath = Paths.get(logsDirectoryPath, logFileName);
      File logFile = logFilePath.toFile();

      if (!logFile.exists() || !logFile.canRead()) {
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
          .contentType(MediaType.TEXT_PLAIN)
          .body(resource);

    } catch (java.time.format.DateTimeParseException e) {
      return ResponseEntity.badRequest().build();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}