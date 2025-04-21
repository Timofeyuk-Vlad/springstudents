package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.*; // Импорт всех DTO
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.StudentMapper;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
@Transactional
public class StudentServiceImpl implements StudentService {

    // --- Константы для сообщений ---
    private static final String STUDENT_NOT_FOUND_BY_ID_MSG = "Student with id %d not found";
    private static final String STUDENT_NOT_FOUND_BY_EMAIL_MSG = "Student with email %s not found";
    private static final String EMAIL_EXISTS_MSG = "Student with email %s already exists.";
    private static final String OTHER_EMAIL_EXISTS_MSG = "Another student with email %s already exists.";
    // --------------------------------

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findAllStudentsSummary() {
        List<Student> students = repository.findAll(); // Загружаем без деталей
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findStudentDetailsById(Long id) {
        // Используем findById, который с @EntityGraph загрузит все связи
        return repository.findById(id)
            .map(mapper::toDetailsDto) // Маппим в детальный DTO
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));
    }

    @Override
    public StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto) {
        if (repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException(String.format(EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }
        Student student = mapper.toEntity(studentDto);
        Student savedStudent = repository.save(student);
        // После сохранения загружаем снова с деталями (findById использует @EntityGraph)
        // или маппим то, что есть, но коллекции будут пустыми, если нет каскадного сохранения
        // Лучше перезагрузить, чтобы получить актуальные данные связей, если они важны сразу
        return findStudentDetailsById(savedStudent.getId());
        // Альтернатива (если связи не важны сразу): return mapper.toDetailsDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findDtoByEmail(String email) { // Возвращаем детали
        Student student = repository.findStudentByEmail(email);
        if (student == null) {
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        // Перезагружаем с деталями, т.к. findStudentByEmail не грузит связи
        return findStudentDetailsById(student.getId());
    }

    @Override
    public StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto) {
        Student existingStudent = repository.findById(id) // findById загрузит все связи
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));

        if (!existingStudent.getEmail().equals(studentDto.getEmail()) &&
            repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException(String.format(OTHER_EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }

        mapper.updateEntityFromDto(studentDto, existingStudent);
        Student updatedStudent = repository.save(existingStudent);
        // Маппим уже обновленную сущность со всеми подгруженными связями
        return mapper.toDetailsDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id));
        }
        repository.deleteById(id);
    }
}