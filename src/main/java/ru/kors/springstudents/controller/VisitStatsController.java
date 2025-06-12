package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.stream.Collectors;
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
      description = "Возвращает количество посещений для полного пути запроса, сформированного как 'HTTP_МЕТОД /uri_путь'")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Количество посещений",
          content = @Content(schema = @Schema(type = "integer", format = "int64")))
  })
  @GetMapping("/path")
  public ResponseEntity<Long> getVisitsForPath(
      @Parameter(description = "Ключ посещения (например, 'GET /api/v1/students')", required = true)
      @RequestParam String requestPath) {
    return ResponseEntity.ok(visitCounterService.getVisitsForPath(requestPath));
  }

  @Operation(summary = "Получить статистику посещений для всех URL")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Статистика по всем путям",
          content = @Content(schema = @Schema(type = "object")))
  })
  @GetMapping("/all")
  public ResponseEntity<Map<String, Long>> getAllVisitCounts() {
    return ResponseEntity.ok(visitCounterService.getAllVisitCounts());
  }

  @Operation(summary = "Получить общее количество всех посещений")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Общее количество посещений",
          content = @Content(schema = @Schema(type = "integer", format = "int64")))
  })
  @GetMapping("/total")
  public ResponseEntity<Long> getTotalVisits() {
    return ResponseEntity.ok(visitCounterService.getTotalVisits());
  }

  @Operation(summary = "Получить статистику посещений по HTTP-методам",
      description = "Группирует статистику по HTTP-методам (GET, POST, PUT и т.д.)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Статистика по методам",
          content = @Content(schema = @Schema(type = "object")))
  })
  @GetMapping("/by-method")
  public ResponseEntity<Map<String, Long>> getVisitsByMethod() {
    Map<String, Long> methodStats = visitCounterService.getAllVisitCounts().entrySet().stream()
        .collect(Collectors.groupingBy(
            entry -> entry.getKey().split(" ")[0],
            Collectors.summingLong(Map.Entry::getValue)
        ));
    return ResponseEntity.ok(methodStats);
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