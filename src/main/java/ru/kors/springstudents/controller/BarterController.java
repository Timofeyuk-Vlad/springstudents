package ru.kors.springstudents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Заменил на явные импорты
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.dto.ErrorResponseDto; // Добавь для описания ошибок
import ru.kors.springstudents.service.BarterService;

import java.util.List;

@Tag(name = "Barter API", description = "API для управления предложениями обмена")
@RestController
@RequestMapping("/api/v1/barters")
@RequiredArgsConstructor
public class BarterController {

    private final BarterService service;

    @Operation(summary = "Получить список всех предложений обмена")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список предложений обмена",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BarterDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<BarterDto>> findAllBarters() {
        return ResponseEntity.ok(service.findAllBarters());
    }

    @Operation(summary = "Создать новое предложение обмена")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Предложение обмена успешно создано",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BarterDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<BarterDto> saveBarter(
        @Parameter(description = "Данные для создания предложения обмена", required = true,
            schema = @Schema(implementation = CreateBarterRequestDto.class))
        @Valid @RequestBody CreateBarterRequestDto barterDto) {
        BarterDto savedBarter = service.saveBarter(barterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBarter);
    }

    @Operation(summary = "Найти предложение обмена по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Предложение обмена найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BarterDto.class))),
        @ApiResponse(responseCode = "404", description = "Предложение обмена не найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BarterDto> findBarterById(
        @Parameter(description = "ID предложения обмена для поиска", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.findBarterById(id));
    }

    @Operation(summary = "Обновить существующее предложение обмена")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Предложение обмена успешно обновлено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BarterDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Предложение обмена или связанный студент не найдены",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BarterDto> updateBarter(
        @Parameter(description = "ID предложения обмена для обновления", required = true) @PathVariable Long id,
        @Parameter(description = "Данные для обновления предложения обмена", required = true,
            schema = @Schema(implementation = CreateBarterRequestDto.class)) // Используем CreateDTO, можно создать отдельный UpdateDTO
        @Valid @RequestBody CreateBarterRequestDto barterDto) {
        return ResponseEntity.ok(service.updateBarter(id, barterDto));
    }

    @Operation(summary = "Удалить предложение обмена по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Предложение обмена успешно удалено"),
        @ApiResponse(responseCode = "404", description = "Предложение обмена не найдено",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarter(
        @Parameter(description = "ID предложения обмена для удаления", required = true) @PathVariable Long id) {
        service.deleteBarter(id);
        return ResponseEntity.noContent().build();
    }
}