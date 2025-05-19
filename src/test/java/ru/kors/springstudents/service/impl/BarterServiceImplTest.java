package ru.kors.springstudents.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarterServiceImplTest {

  @Mock
  private BarterRepository barterRepository;

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private BarterMapper barterMapper;

  @InjectMocks
  private BarterServiceImpl barterService;

  @Test
  void findAllBarters_ShouldReturnAllBarters() {
    // Arrange
    Barter barter1 = new Barter(1L, "Book", "Good condition", "Available", null);
    Barter barter2 = new Barter(2L, "Laptop", "Used", "Pending", null);
    List<Barter> barters = List.of(barter1, barter2, barter1); // duplicate to test Set

    when(barterRepository.findAll()).thenReturn(barters);
    when(barterMapper.toDtoList(anySet())).thenAnswer(invocation -> {
      Set<Barter> input = invocation.getArgument(0);
      return input.stream()
          .map(b -> new BarterDto(b.getId(), b.getItem(), b.getDescription(), b.getStatus(), null))
          .toList();
    });

    // Act
    List<BarterDto> result = barterService.findAllBarters();

    // Assert
    assertThat(result).hasSize(2);
    verify(barterRepository).findAll();
    verify(barterMapper).toDtoList(anySet());
  }

  @Test
  void findBarterById_WhenBarterExists_ShouldReturnBarter() {
    // Arrange
    Long barterId = 1L;
    Barter barter = new Barter(barterId, "Book", "Good condition", "Available", null);
    BarterDto barterDto = new BarterDto(barterId, "Book", "Good condition", "Available", null);

    when(barterRepository.findById(barterId)).thenReturn(Optional.of(barter));
    when(barterMapper.toDto(barter)).thenReturn(barterDto);

    // Act
    BarterDto result = barterService.findBarterById(barterId);

    // Assert
    assertThat(result).isEqualTo(barterDto);
    verify(barterRepository).findById(barterId);
    verify(barterMapper).toDto(barter);
  }

  @Test
  void findBarterById_WhenBarterNotExists_ShouldThrowException() {
    // Arrange
    Long barterId = 999L;
    when(barterRepository.findById(barterId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> barterService.findBarterById(barterId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Barter not found with id: " + barterId);

    verify(barterRepository).findById(barterId);
    verifyNoInteractions(barterMapper);
  }

  @Test
  void saveBarter_WithValidData_ShouldSaveAndReturnBarter() {
    // Arrange
    Long studentId = 1L;
    CreateBarterRequestDto requestDto = new CreateBarterRequestDto("Book", "Good condition", "Available", studentId);

    Student student = new Student(studentId, "John", "Doe", "john@example.com");
    Barter barterToSave = new Barter(null, "Book", "Good condition", "Available", null);
    Barter savedBarter = new Barter(1L, "Book", "Good condition", "Available", student);
    BarterDto expectedDto = new BarterDto(1L, "Book", "Good condition", "Available", null);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(barterMapper.toEntity(requestDto)).thenReturn(barterToSave);
    when(barterRepository.save(barterToSave)).thenReturn(savedBarter);
    when(barterMapper.toDto(savedBarter)).thenReturn(expectedDto);

    // Act
    BarterDto result = barterService.saveBarter(requestDto);

    // Assert
    assertThat(result).isEqualTo(expectedDto);
    assertThat(barterToSave.getStudent()).isEqualTo(student);
    verify(studentRepository).findById(studentId);
    verify(barterMapper).toEntity(requestDto);
    verify(barterRepository).save(barterToSave);
    verify(barterMapper).toDto(savedBarter);
  }

  @Test
  void saveBarter_WithInvalidStudentId_ShouldThrowException() {
    // Arrange
    Long invalidStudentId = 999L;
    CreateBarterRequestDto requestDto = new CreateBarterRequestDto("Book", "Good condition", "Available", invalidStudentId);

    when(studentRepository.findById(invalidStudentId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> barterService.saveBarter(requestDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Student not found with id: " + invalidStudentId);

    verify(studentRepository).findById(invalidStudentId);
    verifyNoInteractions(barterRepository, barterMapper);
  }

  @Test
  void updateBarter_WithValidData_ShouldUpdateAndReturnBarter() {
    // Arrange
    Long barterId = 1L;
    Long studentId = 1L;
    Long newStudentId = 2L;

    CreateBarterRequestDto requestDto = new CreateBarterRequestDto("Updated Book", "Excellent", "Sold", newStudentId);

    Student oldStudent = new Student(studentId, "John", "Doe", "john@example.com");
    Student newStudent = new Student(newStudentId, "Jane", "Doe", "jane@example.com");

    Barter existingBarter = new Barter(barterId, "Book", "Good", "Available", oldStudent);
    Barter updatedBarter = new Barter(barterId, "Updated Book", "Excellent", "Sold", newStudent);
    BarterDto expectedDto = new BarterDto(barterId, "Updated Book", "Excellent", "Sold", null);

    when(barterRepository.findById(barterId)).thenReturn(Optional.of(existingBarter));
    when(studentRepository.findById(newStudentId)).thenReturn(Optional.of(newStudent));
    when(barterRepository.save(existingBarter)).thenReturn(updatedBarter);
    when(barterMapper.toDto(updatedBarter)).thenReturn(expectedDto);

    BarterDto result = barterService.updateBarter(barterId, requestDto);

    assertThat(result).isEqualTo(expectedDto);
    assertThat(existingBarter.getStudent()).isEqualTo(newStudent);
    verify(barterRepository).findById(barterId);
    verify(studentRepository).findById(newStudentId);
    verify(barterRepository).save(existingBarter);
    verify(barterMapper).toDto(updatedBarter);
  }

  @Test
  void updateBarter_WhenBarterNotExists_ShouldThrowException() {
    // Arrange
    Long invalidBarterId = 999L;
    CreateBarterRequestDto requestDto = new CreateBarterRequestDto("Book", "Desc", "Status", 1L);

    when(barterRepository.findById(invalidBarterId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> barterService.updateBarter(invalidBarterId, requestDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Barter not found with id: " + invalidBarterId);

    verify(barterRepository).findById(invalidBarterId);
    verifyNoInteractions(studentRepository, barterMapper);
  }

  @Test
  void updateBarter_WithSameStudent_ShouldNotFetchStudentAgain() {
    // Arrange
    Long barterId = 1L;
    Long studentId = 1L;

    CreateBarterRequestDto requestDto = new CreateBarterRequestDto("Updated", "Desc", "Status", studentId);

    Student student = new Student(studentId, "John", "Doe", "john@example.com");
    Barter existingBarter = new Barter(barterId, "Book", "Good", "Available", student);
    Barter updatedBarter = new Barter(barterId, "Updated", "Desc", "Status", student);
    BarterDto expectedDto = new BarterDto(barterId, "Updated", "Desc", "Status", null);

    when(barterRepository.findById(barterId)).thenReturn(Optional.of(existingBarter));
    when(barterRepository.save(existingBarter)).thenReturn(updatedBarter);
    when(barterMapper.toDto(updatedBarter)).thenReturn(expectedDto);

    // Act
    BarterDto result = barterService.updateBarter(barterId, requestDto);

    // Assert
    assertThat(result).isEqualTo(expectedDto);
    verify(barterRepository).findById(barterId);
    verifyNoInteractions(studentRepository); // Should not fetch student if ID is same
    verify(barterRepository).save(existingBarter);
    verify(barterMapper).toDto(updatedBarter);
  }

  @Test
  void deleteBarter_WhenBarterExists_ShouldDeleteBarter() {
    // Arrange
    Long barterId = 1L;
    when(barterRepository.existsById(barterId)).thenReturn(true);

    // Act
    barterService.deleteBarter(barterId);

    // Assert
    verify(barterRepository).existsById(barterId);
    verify(barterRepository).deleteById(barterId);
  }

  @Test
  void deleteBarter_WhenBarterNotExists_ShouldThrowException() {
    // Arrange
    Long invalidBarterId = 999L;
    when(barterRepository.existsById(invalidBarterId)).thenReturn(false);

    // Act & Assert
    assertThatThrownBy(() -> barterService.deleteBarter(invalidBarterId))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Barter not found with id: " + invalidBarterId);

    verify(barterRepository).existsById(invalidBarterId);
    verifyNoMoreInteractions(barterRepository);
  }
}