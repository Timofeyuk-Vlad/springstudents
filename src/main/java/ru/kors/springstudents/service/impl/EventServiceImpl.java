package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Primary
public class EventServiceImpl implements EventService {

    private static final String EVENT_NOT_FOUND_MSG = "Event not found with id: ";
    private static final String STUDENT_NOT_FOUND_MSG = "Student not found with id: ";


    private final EventRepository repository;
    private final StudentRepository studentRepository;
    private final EventMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findAllEvents() {
        List<Event> eventList = repository.findAll();
        Set<Event> eventSet = new HashSet<>(eventList);
        return mapper.toDtoList(eventSet);
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto findEventById(Long id) {
        // Загружаем событие вместе со студентами, если нужно (используем EntityGraph или JOIN FETCH в репозитории)
        // Или оставляем LAZY, тогда студенты подгрузятся при вызове mapper.toDto внутри транзакции
        return repository.findById(id) // Предполагаем, что findById загрузит студентов (LAZY или EAGER)
            .map(mapper::toDto) // Маппер вызовет getStudents(), что подгрузит их, если LAZY
            .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND_MSG + id));
    }


    @Override
    @Transactional
    public EventDto saveEvent(CreateEventRequestDto eventDto) {
        Event event = mapper.toEntity(eventDto);

        List<Student> studentList = studentRepository.findAllById(eventDto.getStudentIds());
        if (studentList.size() != eventDto.getStudentIds().size()) {
            throw new ResourceNotFoundException("One or more students not found for the provided IDs.");
        }
        Set<Student> students = new HashSet<>(studentList);
        event.setStudents(students);
        Event savedEvent = repository.save(event);
        return mapper.toDto(savedEvent);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long id, CreateEventRequestDto eventDto) {
        Event existingEvent = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(EVENT_NOT_FOUND_MSG + id));

        mapper.updateEntityFromDto(eventDto, existingEvent);

        List<Student> studentList = studentRepository.findAllById(eventDto.getStudentIds());
        if (studentList.size() != eventDto.getStudentIds().size()) {
            throw new ResourceNotFoundException("One or more students not found for the provided IDs during update.");
        }
        Set<Student> students = new HashSet<>(studentList);
        existingEvent.setStudents(students);

        Event updatedEvent = repository.save(existingEvent);
        return mapper.toDto(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(EVENT_NOT_FOUND_MSG + id);
        }
        repository.deleteById(id);
    }
}