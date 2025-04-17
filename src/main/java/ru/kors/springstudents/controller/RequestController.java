package ru.kors.springstudents.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kors.springstudents.dto.CreateRequestDto;
import ru.kors.springstudents.dto.RequestDto;
import ru.kors.springstudents.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService service;

    @GetMapping
    public ResponseEntity<List<RequestDto>> findAllRequests() {
        return ResponseEntity.ok(service.findAllRequests());
    }

    @PostMapping
    public ResponseEntity<RequestDto> saveRequest(@Valid @RequestBody CreateRequestDto requestDto) {
        RequestDto savedRequest = service.saveRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDto> findRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findRequestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestDto> updateRequest(@PathVariable Long id,
                                                    @Valid @RequestBody
                                                    CreateRequestDto requestDto) {
        return ResponseEntity.ok(service.updateRequest(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}