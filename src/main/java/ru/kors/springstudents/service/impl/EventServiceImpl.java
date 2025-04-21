package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.EventMapper;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.EventRepository;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.EventService;

import java.util.List;
import java.util.stream.Collectors; // Collectors нужен для join

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private static final String EVENT_NOT_FOUND_MSG = "Event with id %d not found";
    private static final String STUDENTS_NOT_FOUND_MSG = "Students with ids [%s] not found for Event";
    private static final String STUDENT_UPDATE_NOT_FOUND_MSG = "Some students not found for Event update";

    private final EventRepository repository;
    private final StudentRepository studentRepository;
    private final EventMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findAllEvents() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public EventDto saveEvent(CreateEventRequestDto eventDto) {
        Event event = mapper.toEntity(eventDto);

        if (eventDto.getStudentIds() != null && !eventDto.getStudentIds().isEmpty()) {
            List<Student> students = studentRepository.findAllById(eventDto.getStudentIds());
            if (students.size() != eventDto.getStudentIds().size()) {
                List<Long> foundIds = students.stream().map(Student::getId).toList();
                String notFoundIdsString = eventDto.getStudentIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
                throw new ResourceNotFoundException(String.format(STUDENTS_NOT_FOUND_MSG, notFoundIdsString));
            }
            event.setStudents(students);
        }

        Event savedEvent = repository.save(event);
        return mapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto findEventById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(EVENT_NOT_FOUND_MSG, id)));
    }

    @Override
    public EventDto updateEvent(Long id, CreateEventRequestDto eventDto) {
        Event existingEvent = repository.findById(id)
            // Используем константу и форматирование
            .orElseThrow(() -> new ResourceNotFoundException(String.format(EVENT_NOT_FOUND_MSG, id)));

        mapper.updateEntityFromDto(eventDto, existingEvent);

        existingEvent.getStudents().clear();
        if (eventDto.getStudentIds() != null && !eventDto.getStudentIds().isEmpty()) {
            List<Student> students = studentRepository.findAllById(eventDto.getStudentIds());
            if (students.size() != eventDto.getStudentIds().size()) {
                // Используем константу
                throw new ResourceNotFoundException(STUDENT_UPDATE_NOT_FOUND_MSG);
            }
            existingEvent.setStudents(students);
        }

        Event updatedEvent = repository.save(existingEvent);
        return mapper.toDto(updatedEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        Event event = repository.findById(id)
            // Используем константу и форматирование
            .orElseThrow(() -> new ResourceNotFoundException(String.format(EVENT_NOT_FOUND_MSG, id)));

        event.getStudents().clear();
        repository.save(event);

        repository.deleteById(id);
    }
}