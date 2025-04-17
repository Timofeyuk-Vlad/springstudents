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
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @GetMapping
    public ResponseEntity<List<EventDto>> findAllEvents() {
        return ResponseEntity.ok(service.findAllEvents());
    }

    @PostMapping
    public ResponseEntity<EventDto> saveEvent(@Valid @RequestBody CreateEventRequestDto eventDto) {
        EventDto savedEvent = service.saveEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> findEventById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id,
                                                @Valid @RequestBody
                                                CreateEventRequestDto eventDto) {
        return ResponseEntity.ok(service.updateEvent(id, eventDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}