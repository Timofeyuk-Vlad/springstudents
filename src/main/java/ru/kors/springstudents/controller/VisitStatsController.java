package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.service.VisitCounterService;

@Tag(name = "Visit Statistics API", description = "API для просмотра и управления статистикой посещений")
@RestController
@RequestMapping("/api/v1/stats/visits")
@RequiredArgsConstructor
public class VisitStatsController {

  private final VisitCounterService visitCounterService;

  @Operation(summary = "Получить количество посещений для конкретного пути",
      description = "Возвращает количество посещений для полного пути запроса, сформированного как 'HTTP_МЕТОД /uri_путь'. "
          + "Пример: для GET /api/v1/students, requestPath будет 'GET /api/v1/students'. "
          + "Пробелы и другие спецсимволы в пути должны быть URL-кодированы клиентом (например, пробел как %20, / как %2F). "
          + "Spring автоматически декодирует их перед передачей в метод.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Количество посещений",
          content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64")))
  })
  @GetMapping("/path")
  public ResponseEntity<Long> getVisitsForPath(
      @Parameter(description = "Ключ посещения (например, 'GET /api/v1/students' или 'POST /api/v1/events/1'). "
          + "Пробелы будут автоматически заменены на %20 клиентом.",
          required = true, example = "GET /api/v1/students")
      @RequestParam String requestPath) {
    return ResponseEntity.ok(visitCounterService.getVisitsForPath(requestPath));
  }

  @Operation(summary = "Получить статистику посещений для всех URL",
      description = "Возвращает карту, где ключ - это 'HTTP_МЕТОД /uri_путь', а значение - количество посещений.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Статистика по всем путям",
          content = @Content(mediaType = "application/json",
              schema = @Schema(type = "object")))
  })
  @GetMapping("/all")
  public ResponseEntity<Map<String, Long>> getAllVisitCounts() {
    return ResponseEntity.ok(visitCounterService.getAllVisitCounts());
  }

  @Operation(summary = "Получить общее количество всех посещений")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Общее количество посещений",
          content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64")))
  })
  @GetMapping("/total")
  public ResponseEntity<Long> getTotalVisits() {
    return ResponseEntity.ok(visitCounterService.getTotalVisits());
  }

  @Operation(summary = "Очистить всю статистику посещений")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Статистика успешно очищена")
  })
  @DeleteMapping("/clear")
  public ResponseEntity<Void> clearAllVisitCounts() {
    visitCounterService.clearAllVisitCounts();
    return ResponseEntity.noContent().build();
  }
}