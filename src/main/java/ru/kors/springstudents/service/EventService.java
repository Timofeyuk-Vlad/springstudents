package ru.kors.springstudents.service;

import java.util.List;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;

public interface EventService {
    List<EventDto> findAllEvents();

    EventDto saveEvent(CreateEventRequestDto eventDto);

    EventDto findEventById(Long id);

    EventDto updateEvent(Long id, CreateEventRequestDto eventDto);

    void deleteEvent(Long id);
}