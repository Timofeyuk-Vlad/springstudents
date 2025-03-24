package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.repository.EventRepository;
import ru.kors.springstudents.service.EventService;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public List<Event> findAllEvents() {
        return repository.findAll();
    }

    @Override
    public Event saveEvent(Event event) {
        return repository.save(event);
    }

    @Override
    public Event findEventById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Event updateEvent(Event event) {
        return repository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}