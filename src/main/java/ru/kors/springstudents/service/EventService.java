package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
// import ru.kors.springstudents.dto.UpdateEventRequestDto;

import java.util.List;

public interface EventService {
    List<EventDto> findAllEvents();

    EventDto saveEvent(CreateEventRequestDto eventDto);

    EventDto findEventById(Long id);

    EventDto updateEvent(Long id, CreateEventRequestDto eventDto);

    void deleteEvent(Long id);
}