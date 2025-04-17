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
import ru.kors.springstudents.dto.CreateDutyRequestDto;
import ru.kors.springstudents.dto.DutyDto;
import ru.kors.springstudents.service.DutyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/duties")
@RequiredArgsConstructor
public class DutyController {

    private final DutyService service;

    @GetMapping
    public ResponseEntity<List<DutyDto>> findAllDuties() {
        return ResponseEntity.ok(service.findAllDuties());
    }

    @PostMapping
    public ResponseEntity<DutyDto> saveDuty(@Valid @RequestBody CreateDutyRequestDto dutyDto) {
        DutyDto savedDuty = service.saveDuty(dutyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDuty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DutyDto> findDutyById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDutyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DutyDto> updateDuty(@PathVariable Long id,
                                              @Valid @RequestBody CreateDutyRequestDto dutyDto) {
        return ResponseEntity.ok(service.updateDuty(id, dutyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDuty(@PathVariable Long id) {
        service.deleteDuty(id);
        return ResponseEntity.noContent().build();
    }
}