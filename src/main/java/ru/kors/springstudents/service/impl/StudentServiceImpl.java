package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
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

    private final StudentRepository repository;
    private final StudentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllStudent() {
        List<Student> students = repository.findAll();
        return mapper.toDtoList(students);
    }

    @Override
    public StudentDto saveStudent(CreateStudentRequestDto studentDto) {
        if (repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException("Student with email "
                + studentDto.getEmail() + " already exists.");
        }
        Student student = mapper.toEntity(studentDto);
        Student savedStudent = repository.save(student);
        return mapper.toDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findDtoByEmail(String email) {
        Student student = repository.findStudentByEmail(email);
        if (student == null) {
            throw new ResourceNotFoundException("Student with email " + email + " not found");
        }
        return mapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto findDtoById(Long id) {
        Student student = findStudentEntityById(id);
        return mapper.toDto(student);
    }

    @Override
    public StudentDto updateStudent(Long id, UpdateStudentRequestDto studentDto) {
        Student existingStudent = findStudentEntityById(id);

        if (!existingStudent.getEmail().equals(studentDto.getEmail())
            && repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException("Another student with email "
                + studentDto.getEmail() + " already exists.");
        }

        mapper.updateEntityFromDto(studentDto, existingStudent);

        Student updatedStudent = repository.save(existingStudent);
        return mapper.toDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    private Student findStudentEntityById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student with id "
                + id + " not found"));
    }
}