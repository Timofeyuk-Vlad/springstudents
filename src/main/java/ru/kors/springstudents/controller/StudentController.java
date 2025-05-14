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
import org.springframework.web.bind.annotation.*;
import ru.kors.springstudents.dto.*;
import ru.kors.springstudents.service.StudentService;

import java.util.List;

@Tag(name = "Student API", description = "API для управления студентами")
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @Operation(summary = "Получить краткую сводку по всем студентам")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список студентов (сводка)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentSummaryDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<StudentSummaryDto>> findAllStudentsSummary() {
        return ResponseEntity.ok(service.findAllStudentsSummary());
    }

    @Operation(summary = "Получить полную информацию по всем студентам")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список студентов (детали)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDetailsDto.class)))
    })
    @GetMapping("/details")
    public ResponseEntity<List<StudentDetailsDto>> findAllStudentsDetails() {
        List<StudentDetailsDto> studentsDetails = service.findAllStudentsDetails();
        return ResponseEntity.ok(studentsDetails);
    }

    @Operation(summary = "Найти студента по email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Студент найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDetailsDto.class))),
        @ApiResponse(responseCode = "404", description = "Студент не найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDetailsDto> findByEmail(
        @Parameter(description = "Email студента для поиска", required = true) @PathVariable String email) {
        return ResponseEntity.ok(service.findDtoByEmail(email));
    }

    @Operation(summary = "Найти студента по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Студент найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDetailsDto.class))),
        @ApiResponse(responseCode = "404", description = "Студент не найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> findById(
        @Parameter(description = "ID студента для поиска", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.findStudentDetailsById(id));
    }

    @Operation(summary = "Обновить существующего студента")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Студент успешно обновлен",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDetailsDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Студент не найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Конфликт (например, новый email уже занят)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> updateStudent(
        @Parameter(description = "ID студента для обновления", required = true) @PathVariable Long id,
        @Parameter(description = "Данные для обновления студента", required = true,
            schema = @Schema(implementation = UpdateStudentRequestDto.class))
        @Valid @RequestBody UpdateStudentRequestDto studentRequest) {
        return ResponseEntity.ok(service.updateStudent(id, studentRequest));
    }

    @Operation(summary = "Удалить студента по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Студент успешно удален"),
        @ApiResponse(responseCode = "404", description = "Студент не найден",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
        @Parameter(description = "ID студента для удаления", required = true) @PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Найти студентов по имени события (JPQL)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список студентов (сводка)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentSummaryDto.class)))
    })
    @GetMapping("/search/by-event-name")
    public ResponseEntity<List<StudentSummaryDto>> findStudentsByEventName(
        @Parameter(description = "Имя события для поиска студентов", required = true)
        @RequestParam(name = "name") String eventName) {
        return ResponseEntity.ok(service.findStudentsByEventName(eventName));
    }

    @Operation(summary = "Найти студентов по активному предмету обмена (Native Query)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список студентов (сводка)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentSummaryDto.class)))
    })
    @GetMapping("/search/by-active-barter-item")
    public ResponseEntity<List<StudentSummaryDto>> findStudentsByActiveBarterItem(
        @Parameter(description = "Название предмета для поиска активных обменов", required = true)
        @RequestParam(name = "item") String itemName) {
        return ResponseEntity.ok(service.findStudentsWithActiveBarterByItem(itemName));
    }

    @Operation(summary = "Создать нового студента")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Студент успешно создан",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDetailsDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Конфликт (например, email уже существует)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<StudentDetailsDto> saveStudent(
        @Parameter(description = "Данные для создания студента", required = true,
            schema = @Schema(implementation = CreateStudentRequestDto.class))
        @Valid @RequestBody CreateStudentRequestDto studentRequest) {
        StudentDetailsDto savedStudent = service.saveStudent(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @Operation(summary = "Массовое создание студентов",
        description = "Принимает список DTO для создания нескольких студентов за один запрос.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Студенты успешно созданы",
            content = @Content(mediaType = "application/json",
                schema = @Schema(type = "array", implementation = StudentDetailsDto.class))),
        @ApiResponse(responseCode = "400", description = "Невалидные входные данные для одного " +
            "или нескольких студентов",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Конфликт (например, email уже существует " +
            "для одного из студентов)",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<StudentDetailsDto>> saveStudentsBulk(
        @Parameter(description = "Список DTO для создания студентов. Каждый элемент списка будет провалидирован.",
            required = true)
        @Valid @RequestBody List<CreateStudentRequestDto> studentRequests) {
        List<StudentDetailsDto> savedStudents = service.saveStudentsBulk(studentRequests);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudents);
    }
}