package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.StudentMapper;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.StudentService;

import java.util.List;
// import java.util.stream.Collectors; // Больше не нужен здесь, т.к. используем toDetailsDtoList

@Service
@RequiredArgsConstructor
@Primary
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_FOUND_BY_ID_MSG = "Student with id %d not found";
    private static final String STUDENT_NOT_FOUND_BY_EMAIL_MSG = "Student with email %s not found";
    private static final String EMAIL_EXISTS_MSG = "Student with email %s already exists.";
    private static final String OTHER_EMAIL_EXISTS_MSG = "Another student with email %s already exists.";

    private final StudentRepository repository;
    private final StudentMapper mapper;

    private StudentService self;

    @Autowired
    @Lazy
    public void setSelf(StudentService self) {
        this.self = self;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findAllStudentsSummary() {
        List<Student> students = repository.findAll(); // EntityGraph сработает
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDetailsDto> findAllStudentsDetails() {
        List<Student> students = repository.findAll(); // EntityGraph сработает
        return mapper.toDetailsDtoList(students); // Используем метод маппера для списка
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findStudentDetailsById(Long id) {
        return repository.findById(id) // EntityGraph сработает
            .map(mapper::toDetailsDto)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));
    }

    @Override
    @Transactional
    public StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto) {
        if (repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException(String.format(EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }
        Student student = mapper.toEntity(studentDto);
        Student savedStudent = repository.save(student);
        return self.findStudentDetailsById(savedStudent.getId()); // Получаем детали через self-вызов
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findDtoByEmail(String email) {
        Student student = repository.findStudentByEmail(email);
        if (student == null) {
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return self.findStudentDetailsById(student.getId()); // Получаем детали через self-вызов
    }

    @Override
    @Transactional
    public StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto) {
        Student existingStudent = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));

        if (!existingStudent.getEmail().equals(studentDto.getEmail()) &&
            repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException(String.format(OTHER_EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }

        mapper.updateEntityFromDto(studentDto, existingStudent);
        Student updatedStudent = repository.save(existingStudent);
        // Данные (включая связи, загруженные findById) уже в updatedStudent
        return mapper.toDetailsDto(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id));
        }
        repository.deleteById(id);
    }
}