package ru.kors.springstudents.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kors.springstudents.dto.CreateEventRequestDto;
import ru.kors.springstudents.dto.EventDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.EventMapper;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.EventRepository;
import ru.kors.springstudents.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventServiceImpl Tests")
class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;
  @Mock
  private StudentRepository studentRepository;
  @Mock
  private EventMapper eventMapper;

  @InjectMocks
  private EventServiceImpl eventService;

  @Captor
  private ArgumentCaptor<Event> eventArgumentCaptor;
  @Captor
  private ArgumentCaptor<Set<Event>> eventSetCaptor; // Для проверки toDtoList

  private Event event1, event2;
  private EventDto eventDto1, eventDto2;
  private CreateEventRequestDto createEventRequestDto;
  private Student student1, student2;

  @BeforeEach
  void setUp() {
    student1 = Student.builder().id(1L).firstName("Student1").email("s1@e.com").build();
    student2 = Student.builder().id(2L).firstName("Student2").email("s2@e.com").build();

    event1 = Event.builder()
        .id(1L)
        .name("Event 1")
        .description("Description 1")
        .date(LocalDateTime.now().plusDays(1))
        .students(new HashSet<>(Set.of(student1)))
        .build();

    event2 = Event.builder()
        .id(2L)
        .name("Event 2")
        .description("Description 2")
        .date(LocalDateTime.now().plusDays(2))
        .students(new HashSet<>(Set.of(student1, student2)))
        .build();

    eventDto1 = EventDto.builder()
        .id(1L)
        .name("Event 1")
        .description("Description 1")
        .date(event1.getDate())
        .studentIds(List.of(1L))
        .build();

    eventDto2 = EventDto.builder()
        .id(2L)
        .name("Event 2")
        .description("Description 2")
        .date(event2.getDate())
        .studentIds(List.of(1L, 2L))
        .build();

    createEventRequestDto = CreateEventRequestDto.builder()
        .name("New Event")
        .description("New Description")
        .date(LocalDateTime.now().plusDays(5))
        .studentIds(List.of(1L, 2L))
        .build();
  }

  @Nested
  @DisplayName("Find Operations")
  class FindOperations {
    @Test
    @DisplayName("findAllEvents should return list of EventDtos")
    void findAllEvents_shouldReturnListOfEventDtos() {
      when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
      when(eventMapper.toDtoList(anySet())).thenReturn(List.of(eventDto1, eventDto2));

      List<EventDto> result = eventService.findAllEvents();

      assertNotNull(result);
      assertEquals(2, result.size());
      verify(eventRepository).findAll();
      verify(eventMapper).toDtoList(eventSetCaptor.capture());
      assertTrue(eventSetCaptor.getValue().containsAll(List.of(event1, event2)));
      assertEquals(2, eventSetCaptor.getValue().size());
    }

    @Test
    @DisplayName("findAllEvents should return empty list when no events exist")
    void findAllEvents_shouldReturnEmptyList_whenNoEventsExist() {
      when(eventRepository.findAll()).thenReturn(Collections.emptyList());
      when(eventMapper.toDtoList(Collections.emptySet())).thenReturn(Collections.emptyList());

      List<EventDto> result = eventService.findAllEvents();

      assertNotNull(result);
      assertTrue(result.isEmpty());
      verify(eventRepository).findAll();
      verify(eventMapper).toDtoList(Collections.emptySet());
    }

    @Test
    @DisplayName("findEventById should return EventDto when event exists")
    void findEventById_shouldReturnEventDto_whenEventExists() {
      when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
      when(eventMapper.toDto(event1)).thenReturn(eventDto1);

      EventDto result = eventService.findEventById(1L);

      assertNotNull(result);
      assertEquals(eventDto1.getName(), result.getName());
      verify(eventRepository).findById(1L);
      verify(eventMapper).toDto(event1);
    }

    @Test
    @DisplayName("findEventById should throw ResourceNotFoundException when event does not exist")
    void findEventById_shouldThrowException_whenEventDoesNotExist() {
      when(eventRepository.findById(99L)).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class, () -> eventService.findEventById(99L));
      verify(eventMapper, never()).toDto(any(Event.class));
    }
  }

  @Nested
  @DisplayName("Save Operations")
  class SaveOperations {
    @Test
    @DisplayName("saveEvent should save and return EventDto")
    void saveEvent_shouldSaveAndReturnEventDto() {
      Event eventToSave = Event.builder()
          .name(createEventRequestDto.getName())
          .description(createEventRequestDto.getDescription())
          .date(createEventRequestDto.getDate())
          .build();

      Event savedEventWithId = Event.builder()
          .id(3L) // Предполагаем новый ID
          .name(createEventRequestDto.getName())
          .description(createEventRequestDto.getDescription())
          .date(createEventRequestDto.getDate())
          .students(new HashSet<>(List.of(student1, student2)))
          .build();

      EventDto expectedDto = EventDto.builder()
          .id(3L)
          .name(createEventRequestDto.getName())
          .studentIds(createEventRequestDto.getStudentIds())
          .build();

      when(studentRepository.findAllById(createEventRequestDto.getStudentIds())).thenReturn(List.of(student1, student2));
      when(eventMapper.toEntity(createEventRequestDto)).thenReturn(eventToSave);
      when(eventRepository.save(any(Event.class))).thenReturn(savedEventWithId);
      when(eventMapper.toDto(savedEventWithId)).thenReturn(expectedDto);

      EventDto result = eventService.saveEvent(createEventRequestDto);

      assertNotNull(result);
      assertEquals(expectedDto.getName(), result.getName());
      assertEquals(expectedDto.getStudentIds(), result.getStudentIds());

      verify(studentRepository).findAllById(createEventRequestDto.getStudentIds());
      verify(eventMapper).toEntity(createEventRequestDto);
      verify(eventRepository).save(eventArgumentCaptor.capture());
      Event capturedEvent = eventArgumentCaptor.getValue();
      assertEquals(createEventRequestDto.getName(), capturedEvent.getName());
      assertTrue(capturedEvent.getStudents().containsAll(List.of(student1, student2)));
      verify(eventMapper).toDto(savedEventWithId);
    }

    @Test
    @DisplayName("saveEvent with no student IDs should save event with empty student list")
    void saveEvent_withNoStudentIds_shouldSaveEventWithEmptyStudentList() {
      CreateEventRequestDto dtoWithoutStudents = CreateEventRequestDto.builder()
          .name("Solo Event")
          .date(LocalDateTime.now().plusDays(1))
          .studentIds(Collections.emptyList())
          .build();
      Event eventToSave = Event.builder().name("Solo Event").students(new HashSet<>()).date(dtoWithoutStudents.getDate()).build();
      Event savedEvent = Event.builder().id(1L).name("Solo Event").students(new HashSet<>()).date(dtoWithoutStudents.getDate()).build();
      EventDto expectedDto = EventDto.builder().id(1L).name("Solo Event").studentIds(Collections.emptyList()).date(dtoWithoutStudents.getDate()).build();

      when(eventMapper.toEntity(dtoWithoutStudents)).thenReturn(eventToSave);
      when(studentRepository.findAllById(Collections.emptyList())).thenReturn(Collections.emptyList());
      when(eventRepository.save(eventToSave)).thenReturn(savedEvent);
      when(eventMapper.toDto(savedEvent)).thenReturn(expectedDto);

      EventDto result = eventService.saveEvent(dtoWithoutStudents);

      assertNotNull(result);
      assertTrue(result.getStudentIds().isEmpty());
      verify(studentRepository).findAllById(Collections.emptyList());
      verify(eventRepository).save(eventToSave);
    }

    @Test
    @DisplayName("saveEvent should throw ResourceNotFoundException if any student ID not found")
    void saveEvent_shouldThrowException_ifAnyStudentNotFound() {
      when(studentRepository.findAllById(createEventRequestDto.getStudentIds())).thenReturn(List.of(student1)); // Вернули только одного
      when(eventMapper.toEntity(createEventRequestDto)).thenReturn(new Event());

      assertThrows(ResourceNotFoundException.class, () -> eventService.saveEvent(createEventRequestDto));
      verify(eventRepository, never()).save(any(Event.class));
    }
  }

  @Nested
  @DisplayName("Update Operations")
  class UpdateOperations {
    @Test
    @DisplayName("updateEvent should update and return EventDto")
    void updateEvent_shouldUpdateAndReturnEventDto() {
      CreateEventRequestDto updateDto = CreateEventRequestDto.builder()
          .name("Updated Name")
          .studentIds(List.of(student2.getId())) // Только student2
          .date(LocalDateTime.now().plusHours(1))
          .build();

      Event eventFromRepo = event1; // Обновляем event1
      Event updatedEventInRepo = Event.builder() // То, что вернет save
          .id(event1.getId())
          .name(updateDto.getName())
          .students(new HashSet<>(List.of(student2)))
          .date(updateDto.getDate())
          .build();
      EventDto expectedDto = EventDto.builder()
          .id(event1.getId())
          .name(updateDto.getName())
          .studentIds(List.of(student2.getId()))
          .date(updateDto.getDate())
          .build();

      when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(eventFromRepo));
      doNothing().when(eventMapper).updateEntityFromDto(updateDto, eventFromRepo); // Для void методов
      when(studentRepository.findAllById(List.of(student2.getId()))).thenReturn(List.of(student2));
      when(eventRepository.save(any(Event.class))).thenReturn(updatedEventInRepo);
      when(eventMapper.toDto(updatedEventInRepo)).thenReturn(expectedDto);

      EventDto result = eventService.updateEvent(event1.getId(), updateDto);

      assertNotNull(result);
      assertEquals(expectedDto.getName(), result.getName());
      assertEquals(expectedDto.getStudentIds(), result.getStudentIds());

      verify(eventMapper).updateEntityFromDto(updateDto, eventFromRepo);
      verify(eventRepository).save(eventArgumentCaptor.capture());
      assertTrue(eventArgumentCaptor.getValue().getStudents().contains(student2));
      assertEquals(1, eventArgumentCaptor.getValue().getStudents().size());
    }

    @Test
    @DisplayName("updateEvent should throw ResourceNotFoundException if event not found")
    void updateEvent_shouldThrowException_ifEventNotFound() {
      when(eventRepository.findById(99L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(99L, createEventRequestDto));
    }

    @Test
    @DisplayName("updateEvent should throw ResourceNotFoundException if any student for update not found")
    void updateEvent_shouldThrowException_ifAnyStudentForUpdateNotFound() {
      when(eventRepository.findById(event1.getId())).thenReturn(Optional.of(event1));
      //findAllById вернет пустой список, а в DTO есть ID
      when(studentRepository.findAllById(createEventRequestDto.getStudentIds())).thenReturn(Collections.emptyList());

      assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(event1.getId(), createEventRequestDto));
      verify(eventRepository, never()).save(any(Event.class));
    }
  }

  @Nested
  @DisplayName("Delete Operations")
  class DeleteOperations {
    @Test
    @DisplayName("deleteEvent should call repository deleteById")
    void deleteEvent_shouldCallRepositoryDeleteById() {
      when(eventRepository.existsById(1L)).thenReturn(true);
      doNothing().when(eventRepository).deleteById(1L);

      eventService.deleteEvent(1L);

      verify(eventRepository).existsById(1L);
      verify(eventRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteEvent should throw ResourceNotFoundException if event not found")
    void deleteEvent_shouldThrowException_ifEventNotFound() {
      when(eventRepository.existsById(99L)).thenReturn(false);
      assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(99L));
      verify(eventRepository, never()).deleteById(anyLong());
    }
  }
}