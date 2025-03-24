package ru.kors.springstudents.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
public class EventController {
    private final EventService service;

    @GetMapping
    public List<Event> findAllEvents() {
        return service.findAllEvents();
    }

    @PostMapping
    public Event saveEvent(@RequestBody Event event) {
        return service.saveEvent(event);
    }

    @GetMapping("/{id}")
    public Event findEventById(@PathVariable Long id) {
        return service.findEventById(id);
    }

    @PutMapping
    public Event updateEvent(@RequestBody Event event) {
        return service.updateEvent(event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
    }
}