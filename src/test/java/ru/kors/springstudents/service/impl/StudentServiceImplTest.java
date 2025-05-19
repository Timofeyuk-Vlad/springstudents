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
import ru.kors.springstudents.cache.StudentCache;
import ru.kors.springstudents.dto.*;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.StudentMapper;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.StudentService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentServiceImpl Tests")
class StudentServiceImplTest {

  @Mock
  private StudentRepository studentRepository;
  @Mock
  private StudentMapper studentMapper;
  @Mock
  private StudentCache studentCache;
  @Mock
  private StudentService self;

  @InjectMocks
  private StudentServiceImpl studentService;

  @Captor
  private ArgumentCaptor<Student> studentArgumentCaptor;
  @Captor
  private ArgumentCaptor<Long> longArgumentCaptor;
  @Captor
  private ArgumentCaptor<List<CreateStudentRequestDto>> createStudentListCaptor;


  private Student student1, student2;
  private StudentDetailsDto studentDetailsDto1, studentDetailsDto2;
  private StudentSummaryDto studentSummaryDto1, studentSummaryDto2;
  private CreateStudentRequestDto createStudentRequestDto1, createStudentRequestDto2;
  private UpdateStudentRequestDto updateStudentRequestDto1;

  @BeforeEach
  void setUp() {
    studentService.setSelf(self);

    student1 = Student.builder()
        .id(1L)
        .firstName("ТестИмя1")
        .lastName("ТестФамилия1")
        .email("test1@example.com")
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .events(new HashSet<>())
        .barters(new HashSet<>())
        .build();

    student2 = Student.builder()
        .id(2L)
        .firstName("ТестИмя2")
        .lastName("ТестФамилия2")
        .email("test2@example.com")
        .dateOfBirth(LocalDate.of(2001, 2, 2))
        .events(new HashSet<>())
        .barters(new HashSet<>())
        .build();

    studentDetailsDto1 = StudentDetailsDto.builder()
        .id(1L)
        .firstName("ТестИмя1")
        .lastName("ТестФамилия1")
        .email("test1@example.com")
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .age(student1.getAge()) // Добавим age
        .events(Collections.emptySet()) // Добавим пустые коллекции
        .barters(Collections.emptySet())
        .build();

    studentDetailsDto2 = StudentDetailsDto.builder()
        .id(2L)
        .firstName("ТестИмя2")
        .lastName("ТестФамилия2")
        .email("test2@example.com")
        .dateOfBirth(LocalDate.of(2001, 2, 2))
        .age(student2.getAge())
        .events(Collections.emptySet())
        .barters(Collections.emptySet())
        .build();

    studentSummaryDto1 = StudentSummaryDto.builder()
        .id(1L)
        .firstName("ТестИмя1")
        .lastName("ТестФамилия1")
        .email("test1@example.com")
        .age(student1.getAge()) // Добавим age
        .build();

    studentSummaryDto2 = StudentSummaryDto.builder()
        .id(2L)
        .firstName("ТестИмя2")
        .lastName("ТестФамилия2")
        .email("test2@example.com")
        .age(student2.getAge())
        .build();

    createStudentRequestDto1 = CreateStudentRequestDto.builder()
        .firstName("Новый")
        .lastName("Студент")
        .email("new.student@example.com")
        .dateOfBirth(LocalDate.of(2002, 2, 2))
        .build();

    createStudentRequestDto2 = CreateStudentRequestDto.builder()
        .firstName("ЕщеОдин")
        .lastName("СтудентBulk")
        .email("new.student.bulk@example.com")
        .dateOfBirth(LocalDate.of(2003, 3, 3))
        .build();


    updateStudentRequestDto1 = UpdateStudentRequestDto.builder()
        .firstName("ОбновленноеИмя")
        .lastName("ОбновленнаяФамилия")
        .email("updated.student@example.com")
        .dateOfBirth(LocalDate.of(2000, 1, 1))
        .build();
  }

  @Nested
  @DisplayName("Find Operations")
  class FindOperations {
    // ... (Тесты для find... из твоего предыдущего кода, они выглядят хорошо) ...
    // Я их скопирую и проверю/дополню
    @Test
    @DisplayName("findAllStudentsSummary should return list of summary DTOs")
    void findAllStudentsSummary_shouldReturnListOfSummaryDtos() {
      when(studentRepository.findAll()).thenReturn(List.of(student1, student2));
      when(studentMapper.toSummaryDtoList(List.of(student1, student2))).thenReturn(List.of(studentSummaryDto1, studentSummaryDto2));

      List<StudentSummaryDto> result = studentService.findAllStudentsSummary();

      assertNotNull(result);
      assertEquals(2, result.size());
      verify(studentRepository).findAll();
      verify(studentMapper).toSummaryDtoList(List.of(student1, student2));
    }

    @Test
    @DisplayName("findAllStudentsDetails should return list of details DTOs")
    void findAllStudentsDetails_shouldReturnListOfDetailsDtos() {
      when(studentRepository.findAll()).thenReturn(List.of(student1, student2));
      when(studentMapper.toDetailsDtoList(List.of(student1, student2))).thenReturn(List.of(studentDetailsDto1, studentDetailsDto2));

      List<StudentDetailsDto> result = studentService.findAllStudentsDetails();

      assertNotNull(result);
      assertEquals(2, result.size());
      verify(studentRepository).findAll();
      verify(studentMapper).toDetailsDtoList(List.of(student1, student2));
    }


    @Test
    @DisplayName("findStudentDetailsById should return DTO from cache when present")
    void findStudentDetailsById_shouldReturnDtoFromCache_whenPresent() {
      when(studentCache.get(1L)).thenReturn(Optional.of(studentDetailsDto1));
      StudentDetailsDto result = studentService.findStudentDetailsById(1L);
      assertSame(studentDetailsDto1, result);
      verify(studentRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("findStudentDetailsById should fetch from DB and cache when not in cache")
    void findStudentDetailsById_shouldFetchFromDbAndCache_whenNotInCache() {
      when(studentCache.get(1L)).thenReturn(Optional.empty());
      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      when(studentMapper.toDetailsDto(student1)).thenReturn(studentDetailsDto1);

      StudentDetailsDto result = studentService.findStudentDetailsById(1L);

      assertSame(studentDetailsDto1, result);
      verify(studentRepository).findById(1L);
      verify(studentMapper).toDetailsDto(student1);
      verify(studentCache).put(1L, studentDetailsDto1);
    }

    @Test
    @DisplayName("findStudentDetailsById should throw ResourceNotFoundException when student not found")
    void findStudentDetailsById_shouldThrowException_whenStudentNotFound() {
      when(studentCache.get(1L)).thenReturn(Optional.empty());
      when(studentRepository.findById(1L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> studentService.findStudentDetailsById(1L));
    }

    @Test
    @DisplayName("findDtoByEmail should return details DTO using self injection for cache/entityGraph")
    void findDtoByEmail_shouldReturnDetailsDto() {
      String email = "test1@example.com";
      when(studentRepository.findStudentByEmail(email)).thenReturn(student1);
      when(self.findStudentDetailsById(1L)).thenReturn(studentDetailsDto1); // Мокируем self-вызов

      StudentDetailsDto result = studentService.findDtoByEmail(email);

      assertSame(studentDetailsDto1, result);
      verify(studentRepository).findStudentByEmail(email);
      verify(self).findStudentDetailsById(1L); // Проверяем self-вызов
    }

    @Test
    @DisplayName("findDtoByEmail should throw ResourceNotFoundException when student not found")
    void findDtoByEmail_shouldThrowException_whenStudentNotFound() {
      String email = "nonexistent@example.com";
      when(studentRepository.findStudentByEmail(email)).thenReturn(null);
      assertThrows(ResourceNotFoundException.class, () -> studentService.findDtoByEmail(email));
    }
  }

  @Nested
  @DisplayName("Save Operations")
  class SaveOperations {
    @Test
    @DisplayName("saveStudent should save and return DTO, and cache it")
    void saveStudent_shouldSaveAndReturnDtoAndCache() {
      Student newStudentEntity = Student.builder().email(createStudentRequestDto1.getEmail()).build(); // Упрощено
      Student savedStudentWithId = Student.builder().id(1L).email(createStudentRequestDto1.getEmail()).build();
      StudentDetailsDto expectedDetailsDto = StudentDetailsDto.builder().id(1L).email(createStudentRequestDto1.getEmail()).build();

      when(studentRepository.findStudentByEmail(createStudentRequestDto1.getEmail())).thenReturn(null);
      when(studentMapper.toEntity(createStudentRequestDto1)).thenReturn(newStudentEntity);
      when(studentRepository.save(newStudentEntity)).thenReturn(savedStudentWithId);
      // Изменили: теперь saveStudent маппит сам, а не через self.findStudentDetailsById
      when(studentMapper.toDetailsDto(savedStudentWithId)).thenReturn(expectedDetailsDto);

      StudentDetailsDto result = studentService.saveStudent(createStudentRequestDto1);

      assertNotNull(result);
      assertEquals(expectedDetailsDto.getEmail(), result.getEmail());
      verify(studentRepository).save(newStudentEntity);
      verify(studentCache).put(1L, expectedDetailsDto);
    }

    @Test
    @DisplayName("saveStudent should throw IllegalArgumentException when email exists")
    void saveStudent_shouldThrowException_whenEmailExists() {
      when(studentRepository.findStudentByEmail(createStudentRequestDto1.getEmail())).thenReturn(new Student());
      assertThrows(IllegalArgumentException.class, () -> studentService.saveStudent(createStudentRequestDto1));
    }

    // --- Тесты для saveStudentsBulk ---
    @Test
    @DisplayName("saveStudentsBulk should save each student and put them to cache")
    void saveStudentsBulk_shouldSaveEachAndCache() {
      List<CreateStudentRequestDto> requestDtos = List.of(createStudentRequestDto1, createStudentRequestDto2);

      Student studentEntity1 = Student.builder().email(createStudentRequestDto1.getEmail()).build();
      Student studentEntity2 = Student.builder().email(createStudentRequestDto2.getEmail()).build();

      Student savedEntity1 = Student.builder().id(1L).email(createStudentRequestDto1.getEmail()).build();
      Student savedEntity2 = Student.builder().id(2L).email(createStudentRequestDto2.getEmail()).build();

      StudentDetailsDto detailsDto1 = StudentDetailsDto.builder().id(1L).build();
      StudentDetailsDto detailsDto2 = StudentDetailsDto.builder().id(2L).build();

      when(studentRepository.findStudentByEmail(anyString())).thenReturn(null);
      when(studentMapper.toEntity(createStudentRequestDto1)).thenReturn(studentEntity1);
      when(studentMapper.toEntity(createStudentRequestDto2)).thenReturn(studentEntity2);

      when(studentRepository.save(any(Student.class)))
          .thenReturn(savedEntity1)
          .thenReturn(savedEntity2);

      when(studentMapper.toDetailsDto(savedEntity1)).thenReturn(detailsDto1);
      when(studentMapper.toDetailsDto(savedEntity2)).thenReturn(detailsDto2);

      List<StudentDetailsDto> results = studentService.saveStudentsBulk(requestDtos);

      verify(studentRepository, times(2)).save(any(Student.class));
      verify(studentCache).put(1L, detailsDto1);
      verify(studentCache).put(2L, detailsDto2);
    }

    @Test
    @DisplayName("saveStudentsBulk should throw if duplicate email in request")
    void saveStudentsBulk_duplicateEmailInRequest_shouldThrow() {
      List<CreateStudentRequestDto> requestDtos = List.of(createStudentRequestDto1, createStudentRequestDto1); // Дубликат
      assertThrows(IllegalArgumentException.class, () -> studentService.saveStudentsBulk(requestDtos));
    }

    @Test
    @DisplayName("saveStudentsBulk should throw if email exists in DB")
    void saveStudentsBulk_emailExistsInDb_shouldThrow() {
      when(studentRepository.findStudentByEmail(createStudentRequestDto1.getEmail())).thenReturn(new Student());
      List<CreateStudentRequestDto> requestDtos = List.of(createStudentRequestDto1);
      assertThrows(IllegalArgumentException.class, () -> studentService.saveStudentsBulk(requestDtos));
    }

    @Test
    @DisplayName("saveStudentsBulk with empty list should return empty list")
    void saveStudentsBulk_emptyList_shouldReturnEmptyList() {
      List<StudentDetailsDto> result = studentService.saveStudentsBulk(Collections.emptyList());
      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("saveStudentsBulk with null list should return empty list")
    void saveStudentsBulk_nullList_shouldReturnEmptyList() {
      List<StudentDetailsDto> result = studentService.saveStudentsBulk(null);
      assertTrue(result.isEmpty());
    }
  }

  @Nested
  @DisplayName("Update Operations")
  class UpdateOperations {
    @Test
    @DisplayName("updateStudent should update and return DTO, and update cache")
    void updateStudent_shouldUpdateAndReturnDtoAndUpdateCache() {
      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      when(studentRepository.findStudentByEmail(updateStudentRequestDto1.getEmail())).thenReturn(null);
      when(studentRepository.save(any(Student.class))).thenReturn(student1); // save возвращает обновленный student1
      // Предположим, что studentDetailsDto1 содержит обновленные данные после маппинга
      studentDetailsDto1.setEmail(updateStudentRequestDto1.getEmail()); // Имитируем обновление DTO
      when(studentMapper.toDetailsDto(student1)).thenReturn(studentDetailsDto1);

      StudentDetailsDto result = studentService.updateStudent(1L, updateStudentRequestDto1);

      assertNotNull(result);
      assertEquals(updateStudentRequestDto1.getEmail(), result.getEmail());
      verify(studentRepository).save(student1);
      verify(studentMapper).updateEntityFromDto(updateStudentRequestDto1, student1);
      verify(studentCache).put(1L, studentDetailsDto1);
    }

    @Test
    @DisplayName("updateStudent should throw ResourceNotFoundException when student not found")
    void updateStudent_shouldThrowException_whenStudentNotFound() {
      when(studentRepository.findById(1L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(1L, updateStudentRequestDto1));
    }

    @Test
    @DisplayName("updateStudent should throw IllegalArgumentException when new email is taken by another student")
    void updateStudent_shouldThrowException_whenNewEmailIsTaken() {
      Student otherStudent = Student.builder().id(2L).email(updateStudentRequestDto1.getEmail()).build();
      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      when(studentRepository.findStudentByEmail(updateStudentRequestDto1.getEmail())).thenReturn(otherStudent);

      assertThrows(IllegalArgumentException.class, () -> studentService.updateStudent(1L, updateStudentRequestDto1));
    }

    @Test
    @DisplayName("updateStudent with same email should not check for email existence")
    void updateStudent_withSameEmail_shouldNotCheckEmailExistence() {
      updateStudentRequestDto1.setEmail(student1.getEmail()); // Устанавливаем такой же email
      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      when(studentRepository.save(any(Student.class))).thenReturn(student1);
      when(studentMapper.toDetailsDto(student1)).thenReturn(studentDetailsDto1);

      studentService.updateStudent(1L, updateStudentRequestDto1);

      verify(studentRepository, never()).findStudentByEmail(student1.getEmail()); // Не должно быть этого вызова
      verify(studentRepository).save(student1);
    }
  }

  @Nested
  @DisplayName("Delete Operations")
  class DeleteOperations {
    @Test
    @DisplayName("deleteStudent should delete and evict from cache")
    void deleteStudent_shouldDeleteAndEvictFromCache() {
      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      doNothing().when(studentRepository).delete(student1); // Используем delete(entity)

      studentService.deleteStudent(1L);

      verify(studentRepository).delete(student1); // Проверяем delete(entity)
      verify(studentCache).evict(1L);
    }

    @Test
    @DisplayName("deleteStudent should throw ResourceNotFoundException when student not found")
    void deleteStudent_shouldThrowException_whenStudentNotFound() {
      when(studentRepository.findById(1L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }

    @Test
    @DisplayName("deleteStudent should clear events associations")
    void deleteStudent_shouldClearEvents() {
      // Создаем реальный Event и реальный Set студентов для него
      Event realEvent = new Event();
      realEvent.setId(100L);
      Set<Student> studentsInEvent = new HashSet<>();
      studentsInEvent.add(student1); // Добавляем нашего student1 в событие
      realEvent.setStudents(studentsInEvent);

      // Наш student1 участвует в этом событии
      student1.setEvents(new HashSet<>(Set.of(realEvent)));

      when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
      doNothing().when(studentRepository).delete(student1); // Мокируем удаление студента
      // Не нужно мокировать studentCache.evict(), это void метод, Mockito сам его обработает

      studentService.deleteStudent(1L);

      // Проверяем, что студент был удален из списка участников события
      assertFalse(realEvent.getStudents().contains(student1), "Student should be removed from event's student list");
      // Также проверяем, что у студента очистился список событий
      assertTrue(student1.getEvents().isEmpty(), "Student's event list should be empty after clearing");

      verify(studentRepository).delete(student1);
      verify(studentCache).evict(1L);
    }

    @Nested
    @DisplayName("Custom Query Operations")
    class CustomQueryOperations {
      @Test
      @DisplayName("findStudentsByEventName should call repository and map to summary DTOs")
      void findStudentsByEventName_shouldCallRepositoryAndMap() {
        String eventName = "Конференция";
        when(studentRepository.findStudentsByEventNameJpql(eventName)).thenReturn(List.of(student1, student2));
        when(studentMapper.toSummaryDtoList(List.of(student1, student2))).thenReturn(List.of(studentSummaryDto1, studentSummaryDto2));

        List<StudentSummaryDto> result = studentService.findStudentsByEventName(eventName);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository).findStudentsByEventNameJpql(eventName);
      }

      @Test
      @DisplayName("findStudentsWithActiveBarterByItem should call repository and map to summary DTOs")
      void findStudentsWithActiveBarterByItem_shouldCallRepositoryAndMap() {
        String itemName = "Учебник";
        when(studentRepository.findStudentsWithActiveBarterByItemNative(itemName)).thenReturn(List.of(student1));
        when(studentMapper.toSummaryDtoList(List.of(student1))).thenReturn(List.of(studentSummaryDto1));

        List<StudentSummaryDto> result = studentService.findStudentsWithActiveBarterByItem(itemName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(studentRepository).findStudentsWithActiveBarterByItemNative(itemName);
      }
    }
  }
}