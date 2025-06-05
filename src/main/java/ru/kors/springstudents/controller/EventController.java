package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.ErrorResponseDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.service.EventService;

@Tag(name = "Event API", description = "API для управления событиями")
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @Operation(summary = "Получить список всех событий")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список событий",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<EventDto>> findAllEvents() {
        return ResponseEntity.ok(service.findAllEvents());
    }

    @Operation(summary = "Создать новое событие")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Событие успешно создано",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<EventDto> saveEvent(
        @Parameter(description = "Данные для создания события", required = true,
            schema = @Schema(implementation = CreateEventRequestDto.class))
        @Valid @RequestBody CreateEventRequestDto eventDto) {
        EventDto savedEvent = service.saveEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @Operation(summary = "Найти событие по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findEventById(
        @Parameter(description = "ID события для поиска", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.findEventById(id));
    }

    @Operation(summary = "Обновить существующее событие")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Событие успешно обновлено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EventDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(
        @Parameter(description = "ID события для обновления", required = true) @PathVariable Long id,
        @Parameter(description = "Данные для обновления события", required = true,
            schema = @Schema(implementation = CreateEventRequestDto.class)) // Используем CreateDTO для обновления, можно создать отдельный UpdateDTO
        @Valid @RequestBody CreateEventRequestDto eventDto) {
        return ResponseEntity.ok(service.updateEvent(id, eventDto));
    }

    @Operation(summary = "Удалить событие по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Событие успешно удалено"),
        @ApiResponse(responseCode = "404", description = "Событие не найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
        @Parameter(description = "ID события для удаления", required = true) @PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}