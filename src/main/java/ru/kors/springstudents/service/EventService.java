package ru.kors.springstudents.service;

import ru.kors.springstudents.model.Event;

import java.util.List;

public interface EventService {
    List<Event> findAllEvents();

    Event saveEvent(Event event);

    Event findEventById(Long id);

    Event updateEvent(Event event);

    void deleteEvent(Long id);
}