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
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.BarterMapper;
import ru.kors.springstudents.model.Barter;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.BarterRepository;
import ru.kors.springstudents.repository.StudentRepository;

import java.util.Collections;
import java.util.HashSet; // Импортируем HashSet
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BarterServiceImpl Tests")
class BarterServiceImplTest {

  @Mock
  private BarterRepository barterRepository;
  @Mock
  private StudentRepository studentRepository;
  @Mock
  private BarterMapper barterMapper;

  @InjectMocks
  private BarterServiceImpl barterService;

  @Captor
  private ArgumentCaptor<Barter> barterArgumentCaptor;
  @Captor
  private ArgumentCaptor<Set<Barter>> barterSetCaptor;


  private Barter barter1, barter2;
  private BarterDto barterDto1, barterDto2;
  private CreateBarterRequestDto createBarterRequestDto;
  private Student student1;

  @BeforeEach
  void setUp() {
    student1 = Student.builder().id(1L).firstName("StudentForBarter").build();

    barter1 = Barter.builder()
        .id(1L)
        .item("Книга 1")
        .status("ACTIVE")
        .student(student1)
        .build();

    barter2 = Barter.builder()
        .id(2L)
        .item("Книга 2")
        .status("PENDING")
        .student(student1)
        .build();

    barterDto1 = BarterDto.builder()
        .id(1L)
        .item("Книга 1")
        .status("ACTIVE")
        .studentId(1L)
        .build();

    barterDto2 = BarterDto.builder()
        .id(2L)
        .item("Книга 2")
        .status("PENDING")
        .studentId(1L)
        .build();

    createBarterRequestDto = CreateBarterRequestDto.builder()
        .item("Новый предмет")
        .status("NEW")
        .studentId(1L)
        .description("Описание нового предмета")
        .build();
  }

  @Nested
  @DisplayName("Find Operations")
  class FindOperations {
    @Test
    @DisplayName("findAllBarters should return list of BarterDtos")
    void findAllBarters_shouldReturnListOfBarterDtos() {
      List<Barter> bartersFromRepoAsList = List.of(barter1, barter2);
      Set<Barter> bartersFromRepoAsSet = new HashSet<>(bartersFromRepoAsList); // Создаем Set для мока маппера
      List<BarterDto> expectedDtos = List.of(barterDto1, barterDto2);

      when(barterRepository.findAll()).thenReturn(bartersFromRepoAsList);
      // Теперь маппер ожидает Set
      when(barterMapper.toDtoList(bartersFromRepoAsSet)).thenReturn(expectedDtos);

      List<BarterDto> result = barterService.findAllBarters();

      assertNotNull(result);
      assertEquals(2, result.size());
      verify(barterRepository).findAll();
      verify(barterMapper).toDtoList(barterSetCaptor.capture()); // Используем barterSetCaptor
      assertEquals(bartersFromRepoAsSet, barterSetCaptor.getValue()); // Сравниваем Set'ы
    }

    @Test
    @DisplayName("findAllBarters should return empty list when no barters exist")
    void findAllBarters_shouldReturnEmptyList_whenNoBartersExist() {
      when(barterRepository.findAll()).thenReturn(Collections.emptyList());
      when(barterMapper.toDtoList(Collections.emptySet())).thenReturn(Collections.emptyList());

      List<BarterDto> result = barterService.findAllBarters();

      assertNotNull(result);
      assertTrue(result.isEmpty());
      verify(barterRepository).findAll();
      verify(barterMapper).toDtoList(Collections.emptySet());
    }

    @Test
    @DisplayName("findBarterById should return BarterDto when barter exists")
    void findBarterById_shouldReturnBarterDto_whenBarterExists() {
      when(barterRepository.findById(1L)).thenReturn(Optional.of(barter1));
      when(barterMapper.toDto(barter1)).thenReturn(barterDto1);

      BarterDto result = barterService.findBarterById(1L);

      assertNotNull(result);
      assertEquals(barterDto1.getItem(), result.getItem());
      verify(barterRepository).findById(1L);
      verify(barterMapper).toDto(barter1);
    }

    @Test
    @DisplayName("findBarterById should throw ResourceNotFoundException when barter does not exist")
    void findBarterById_shouldThrowException_whenBarterDoesNotExist() {
      when(barterRepository.findById(99L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> barterService.findBarterById(99L));
    }
  }

  @Nested
  @DisplayName("Save Operations")
  class SaveOperations {
    @Test
    @DisplayName("saveBarter should save and return BarterDto")
    void saveBarter_shouldSaveAndReturnBarterDto() {
      Barter barterToSave = Barter.builder().item(createBarterRequestDto.getItem()).build();
      Barter savedBarterWithId = Barter.builder().id(3L).item(createBarterRequestDto.getItem()).student(student1).build();
      BarterDto expectedDto = BarterDto.builder().id(3L).item(createBarterRequestDto.getItem()).studentId(student1.getId()).build();

      when(studentRepository.findById(createBarterRequestDto.getStudentId())).thenReturn(Optional.of(student1));
      when(barterMapper.toEntity(createBarterRequestDto)).thenReturn(barterToSave);
      when(barterRepository.save(any(Barter.class))).thenReturn(savedBarterWithId);
      when(barterMapper.toDto(savedBarterWithId)).thenReturn(expectedDto);

      BarterDto result = barterService.saveBarter(createBarterRequestDto);

      assertNotNull(result);
      assertEquals(expectedDto.getItem(), result.getItem());
      verify(studentRepository).findById(createBarterRequestDto.getStudentId());
      verify(barterMapper).toEntity(createBarterRequestDto);
      verify(barterRepository).save(barterArgumentCaptor.capture());
      assertEquals(student1, barterArgumentCaptor.getValue().getStudent());
      verify(barterMapper).toDto(savedBarterWithId);
    }

    @Test
    @DisplayName("saveBarter should throw ResourceNotFoundException if student not found")
    void saveBarter_shouldThrowException_ifStudentNotFound() {
      when(studentRepository.findById(createBarterRequestDto.getStudentId())).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class, () -> barterService.saveBarter(createBarterRequestDto));
      verify(barterRepository, never()).save(any(Barter.class));
    }
  }


  @Nested
  @DisplayName("Update Operations")
  class UpdateOperations {
    @Test
    @DisplayName("updateBarter should update and return BarterDto when student is not changed")
    void updateBarter_shouldUpdateAndReturnDto_whenStudentNotChanged() {
      CreateBarterRequestDto updateDto = CreateBarterRequestDto.builder()
          .item("Updated Item Name")
          .description("Updated Description")
          .status("COMPLETED")
          .studentId(student1.getId())
          .build();
      Barter barterFromRepo = barter1;
      Barter updatedBarterInRepo = Barter.builder()
          .id(barter1.getId()).item(updateDto.getItem()).description(updateDto.getDescription())
          .status(updateDto.getStatus()).student(student1)
          .build();
      BarterDto expectedDto = BarterDto.builder()
          .id(barter1.getId()).item(updateDto.getItem()).description(updateDto.getDescription())
          .status(updateDto.getStatus()).studentId(student1.getId())
          .build();

      when(barterRepository.findById(barter1.getId())).thenReturn(Optional.of(barterFromRepo));
      doNothing().when(barterMapper).updateEntityFromDto(updateDto, barterFromRepo);
      when(barterRepository.save(any(Barter.class))).thenReturn(updatedBarterInRepo);
      when(barterMapper.toDto(updatedBarterInRepo)).thenReturn(expectedDto);

      BarterDto result = barterService.updateBarter(barter1.getId(), updateDto);

      assertNotNull(result);
      assertEquals(expectedDto.getItem(), result.getItem());
      assertEquals(expectedDto.getStatus(), result.getStatus());
      verify(studentRepository, never()).findById(anyLong());
      verify(barterRepository).save(barterArgumentCaptor.capture());
      assertEquals(student1, barterArgumentCaptor.getValue().getStudent());
    }

    @Test
    @DisplayName("updateBarter should update student and return BarterDto when studentId is changed")
    void updateBarter_shouldUpdateStudentAndReturnDto_whenStudentIdChanged() {
      Student student2ForUpdate = Student.builder().id(2L).firstName("OtherStudent").build();
      CreateBarterRequestDto updateDtoWithNewStudent = CreateBarterRequestDto.builder()
          .item("Item For Other Student")
          .status("ACTIVE")
          .studentId(student2ForUpdate.getId())
          .build();
      Barter updatedBarterInRepo = Barter.builder()
          .id(barter1.getId()).item(updateDtoWithNewStudent.getItem())
          .student(student2ForUpdate).status(updateDtoWithNewStudent.getStatus())
          .build();
      BarterDto expectedDto = BarterDto.builder()
          .id(barter1.getId()).item(updateDtoWithNewStudent.getItem())
          .studentId(student2ForUpdate.getId()).status(updateDtoWithNewStudent.getStatus())
          .build();

      when(barterRepository.findById(barter1.getId())).thenReturn(Optional.of(barter1));
      when(studentRepository.findById(student2ForUpdate.getId())).thenReturn(Optional.of(student2ForUpdate));
      doNothing().when(barterMapper).updateEntityFromDto(updateDtoWithNewStudent, barter1);
      when(barterRepository.save(any(Barter.class))).thenReturn(updatedBarterInRepo);
      when(barterMapper.toDto(updatedBarterInRepo)).thenReturn(expectedDto);

      BarterDto result = barterService.updateBarter(barter1.getId(), updateDtoWithNewStudent);

      assertNotNull(result);
      assertEquals(expectedDto.getStudentId(), result.getStudentId());
      verify(studentRepository).findById(student2ForUpdate.getId());
      verify(barterRepository).save(barterArgumentCaptor.capture());
      assertEquals(student2ForUpdate, barterArgumentCaptor.getValue().getStudent());
    }

    @Test
    @DisplayName("updateBarter should throw ResourceNotFoundException if barter to update not found")
    void updateBarter_shouldThrowRnfException_ifBarterNotFound() {
      when(barterRepository.findById(99L)).thenReturn(Optional.empty());
      assertThrows(ResourceNotFoundException.class,
          () -> barterService.updateBarter(99L, createBarterRequestDto));
      verify(studentRepository, never()).findById(anyLong());
      verify(barterRepository, never()).save(any(Barter.class));
    }

    @Test
    @DisplayName("updateBarter should throw ResourceNotFoundException if new studentId for update not found")
    void updateBarter_shouldThrowRnfException_ifNewStudentNotFound() {
      CreateBarterRequestDto updateDtoWithNonExistentStudent = CreateBarterRequestDto.builder()
          .studentId(999L) // Несуществующий studentId
          .build();
      when(barterRepository.findById(barter1.getId())).thenReturn(Optional.of(barter1));
      when(studentRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class,
          () -> barterService.updateBarter(barter1.getId(), updateDtoWithNonExistentStudent));
      verify(barterRepository, never()).save(any(Barter.class));
    }
  }

  // ... (DeleteOperations остаются без изменений) ...
  @Nested
  @DisplayName("Delete Operations")
  class DeleteOperations {
    @Test
    @DisplayName("deleteBarter should call repository deleteById")
    void deleteBarter_shouldCallRepositoryDeleteById() {
      when(barterRepository.existsById(1L)).thenReturn(true);
      doNothing().when(barterRepository).deleteById(1L);

      barterService.deleteBarter(1L);

      verify(barterRepository).existsById(1L);
      verify(barterRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBarter should throw ResourceNotFoundException if barter not found")
    void deleteBarter_shouldThrowException_ifBarterNotFound() {
      when(barterRepository.existsById(99L)).thenReturn(false);
      assertThrows(ResourceNotFoundException.class, () -> barterService.deleteBarter(99L));
      verify(barterRepository, never()).deleteById(anyLong());
    }
  }
}