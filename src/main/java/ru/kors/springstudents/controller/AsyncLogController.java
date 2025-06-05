package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.dto.ErrorResponseDto;
import ru.kors.springstudents.dto.LogTaskStatusDto;
import ru.kors.springstudents.service.AsyncLogGenerationService;

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
              schema = @Schema(type = "object", example = "{\"taskId\": \"1\", \"message\": \"Задача принята. Обработка запущена.\"}"))),
      @ApiResponse(responseCode = "400", description = "Неверный формат даты",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @PostMapping("/generate-by-date")
  public ResponseEntity<?> requestLogGeneration(
      @Parameter(description = "Дата в формате YYYY-MM-DD", required = true, example = "2025-05-15")
      @RequestParam String dateString) {
    try {
      LocalDate date = LocalDate.parse(dateString);
      String taskId = asyncLogGenerationService.generateLogFileAsync(date);
      Map<String, String> responseBody = Map.of(
          "taskId", taskId,
          "message", "Log generation task accepted. Processing started."
      );
      return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(responseBody);
    } catch (DateTimeParseException e) {
      log.warn("Invalid date format received for log generation: {}. Details: {}", dateString, e.getMessage());
      ErrorResponseDto errorDto = new ErrorResponseDto(
          LocalDateTime.now(),
          HttpStatus.BAD_REQUEST.value(),
          "Bad Request",
          "Invalid date format. Please use YYYY-MM-DD.",
          "/api/v1/async-logs/generate-by-date",
          Collections.singletonList("dateString: " + e.getParsedString() + " - " + e.getMessage())
      );
      return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorDto);
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
  public ResponseEntity<?> getLogGenerationStatus(
      @Parameter(description = "ID задачи генерации лога", required = true)
      @PathVariable String taskId) {
    return asyncLogGenerationService.getTaskStatus(taskId)
        .<ResponseEntity<?>>map(status -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(status))
        .orElseGet(() -> {
          log.warn("Status request for non-existent task ID: {}", taskId);
          ErrorResponseDto errorDto = new ErrorResponseDto(
              LocalDateTime.now(),
              HttpStatus.NOT_FOUND.value(),
              "Not Found",
              "Task with ID " + taskId + " not found.",
              "/api/v1/async-logs/status/" + taskId,
              null
          );
          return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorDto);
        });
  }

  @Operation(summary = "Скачать сгенерированный лог-файл по ID задачи",
      description = "Файл доступен только если статус задачи 'COMPLETED'. "
          + "Если задача в процессе, вернет 202 с текущим статусом задачи.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Лог-файл для скачивания",
          content = @Content(mediaType = "text/plain")), // Для успешного скачивания файла
      @ApiResponse(responseCode = "202", description = "Задача еще в процессе выполнения",
          content = @Content(mediaType = "application/json", // Указываем, что возвращаем JSON
              schema = @Schema(implementation = LogTaskStatusDto.class))),
      @ApiResponse(responseCode = "404", description = "Задача не найдена или файл не найден после завершения",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class))),
      @ApiResponse(responseCode = "500", description = "Ошибка на сервере при подготовке файла",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @GetMapping("/download/{taskId}")
  public ResponseEntity<?> downloadGeneratedLog(
      @Parameter(description = "ID задачи, для которой был сгенерирован лог", required = true)
      @PathVariable String taskId) {

    Optional<LogTaskStatusDto> taskStatusOpt = asyncLogGenerationService.getTaskStatus(taskId);
    String requestPath = "/api/v1/async-logs/download/" + taskId;

    if (taskStatusOpt.isEmpty()) {
      log.warn("Download request for non-existent task ID: {}", taskId);
      ErrorResponseDto errorDto = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Not Found", "Task with ID " + taskId + " not found.", requestPath, null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorDto);
    }

    LogTaskStatusDto taskStatus = taskStatusOpt.get();
    if (taskStatus.getStatus() != LogTaskStatusDto.TaskStatus.COMPLETED) {
      log.info("Download request for task ID {} which is not completed. Status: {}", taskId, taskStatus.getStatus());
      // Явно указываем Content-Type для ответа 202
      return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(taskStatus);
    }

    Optional<String> filePathOpt = asyncLogGenerationService.getGeneratedLogFilePath(taskId);
    if (filePathOpt.isEmpty() || filePathOpt.get().isBlank()) {
      log.error("Task ID {} COMPLETED but file path is missing or blank.", taskId);
      ErrorResponseDto errorDto = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Log file path not found for completed task " + taskId + ".", requestPath, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorDto);
    }

    try {
      Path filePath = Paths.get(filePathOpt.get());
      Resource resource = new FileSystemResource(filePath);

      if (!resource.exists() || !resource.isReadable()) {
        log.error("Generated log file not found or not readable for task ID {}: {}", taskId, filePathOpt.get());
        ErrorResponseDto errorDto = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Not Found", "Generated log file not found or unreadable for task " + taskId + ".", requestPath, null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorDto);
      }

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
      // CONTENT_TYPE для файла устанавливается ниже с .contentType(MediaType.TEXT_PLAIN)
      headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
      headers.add(HttpHeaders.PRAGMA, "no-cache");
      headers.add(HttpHeaders.EXPIRES, "0");

      return ResponseEntity.ok()
          .headers(headers)
          .contentLength(resource.contentLength())
          .contentType(MediaType.TEXT_PLAIN) // Явно для файла
          .body(resource);

    } catch (IOException e) {
      log.error("IO Error preparing file for download, task ID {}: {}", taskId, e.getMessage(), e);
      ErrorResponseDto errorDto = new ErrorResponseDto(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "IO error preparing log file for task " + taskId + ".", requestPath, null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorDto);
    }
  }
}