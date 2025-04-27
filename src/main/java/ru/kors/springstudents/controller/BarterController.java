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
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.service.BarterService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/barters")
@RequiredArgsConstructor
public class BarterController {

    private final BarterService service;

    @GetMapping
    public ResponseEntity<List<BarterDto>> findAllBarters() {
        return ResponseEntity.ok(service.findAllBarters());
    }

    @PostMapping
    public ResponseEntity<BarterDto> saveBarter(
        @Valid @RequestBody CreateBarterRequestDto barterDto) {
        BarterDto savedBarter = service.saveBarter(barterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBarter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarterDto> findBarterById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findBarterById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarterDto> updateBarter(@PathVariable Long id,
                                                  @Valid @RequestBody CreateBarterRequestDto
                                                      barterDto) {
        return ResponseEntity.ok(service.updateBarter(id, barterDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarter(@PathVariable Long id) {
        service.deleteBarter(id);
        return ResponseEntity.noContent().build();
    }
}