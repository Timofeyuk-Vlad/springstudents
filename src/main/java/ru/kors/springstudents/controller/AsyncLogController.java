package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kors.springstudents.dto.ErrorResponseDto;
import ru.kors.springstudents.dto.LogTaskStatusDto;
import ru.kors.springstudents.service.AsyncLogGenerationService;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Async Log Generation API", description = "API для асинхронной генерации и получения лог-файлов")
@RestController
@RequestMapping("/api/v1/async-logs")
@RequiredArgsConstructor
public class AsyncLogController {

  private static final Logger log = LoggerFactory.getLogger(AsyncLogController.class);
  private final AsyncLogGenerationService asyncLogGenerationService;

  @Operation(summary = "Запустить асинхронную генерацию лог-файла по дате",
      description = "Возвращает ID задачи, по которому можно отслеживать статус.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "202", description = "Задача по генерации лога принята",
          content = @Content(mediaType = "application/json",
              schema = @Schema(type = "object", example = "{\"taskId\": \"uuid-string\"}"))),
      @ApiResponse(responseCode = "400", description = "Неверный формат даты",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/generate-by-date")
  public ResponseEntity<?> requestLogGeneration(@RequestParam String dateString) {
    try {
      LocalDate date = LocalDate.parse(dateString);
      String taskId = asyncLogGenerationService.generateLogFileAsync(date);
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("taskId", taskId));
    } catch (DateTimeParseException e) {
      log.warn("Invalid date format received for log generation: {}", dateString, e);
      return ResponseEntity.badRequest().body(
          new ErrorResponseDto(null, HttpStatus.BAD_REQUEST.value(), "Bad Request",
              "Invalid date format. Please use YYYY-MM-DD.", "/api/v1/async-logs/generate-by-date", null)
      );
    }
  }

  @Operation(summary = "Получить статус задачи по генерации лога")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Статус задачи получен",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = LogTaskStatusDto.class))),
      @ApiResponse(responseCode = "404", description = "Задача с таким ID не найдена",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/status/{taskId}")
  public ResponseEntity<LogTaskStatusDto> getLogGenerationStatus(
      @Parameter(description = "ID задачи генерации лога", required = true)
      @PathVariable String taskId) {
    return asyncLogGenerationService.getTaskStatus(taskId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Скачать сгенерированный лог-файл по ID задачи",
      description = "Файл доступен только если статус задачи 'COMPLETED'.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Лог-файл для скачивания",
          content = @Content(mediaType = "text/plain")),
      @ApiResponse(responseCode = "404", description = "Задача не найдена, не завершена или файл не найден",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "202", description = "Задача еще в процессе выполнения (статус не COMPLETED)",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = LogTaskStatusDto.class)))
  })
  @GetMapping("/download/{taskId}")
  public ResponseEntity<Resource> downloadGeneratedLog(
      @Parameter(description = "ID задачи, для которой был сгенерирован лог", required = true)
      @PathVariable String taskId) {

    Optional<LogTaskStatusDto> taskStatusOpt = asyncLogGenerationService.getTaskStatus(taskId);
    if (taskStatusOpt.isEmpty()) {
      log.warn("Download request for non-existent task ID: {}", taskId);
      return ResponseEntity.notFound().build();
    }

    LogTaskStatusDto taskStatus = taskStatusOpt.get();
    if (taskStatus.getStatus() != LogTaskStatusDto.TaskStatus.COMPLETED) {
      log.info("Download request for task ID {} which is not completed. Status: {}", taskId, taskStatus.getStatus());
      // Возвращаем текущий статус и код 202 Accepted
      // или можно вернуть 409 Conflict с информацией, что задача еще не готова
      return ResponseEntity.status(HttpStatus.ACCEPTED).body(null); // Убрали body(taskStatus) для ResponseEntity<Resource>
    }

    Optional<String> filePathOpt = asyncLogGenerationService.getGeneratedLogFilePath(taskId);
    if (filePathOpt.isEmpty() || filePathOpt.get().isBlank()) { // Проверяем, что путь не пустой
      log.error("Task ID {} COMPLETED but file path is missing or blank.", taskId);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Это неожиданная ситуация
    }

    try {
      Path filePath = Paths.get(filePathOpt.get());
      Resource resource = new FileSystemResource(filePath); // Теперь должно работать

      if (!resource.exists() || !resource.isReadable()) {
        throw new FileNotFoundException("Generated log file not found or not readable: " + filePathOpt.get());
      }

      return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
          .body(resource);

    } catch (FileNotFoundException e) {
      log.error("File not found for download, task ID {}: {}", taskId, e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) { // Более общий перехват для других возможных ошибок I/O
      log.error("Error preparing file for download, task ID {}: {}", taskId, e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}