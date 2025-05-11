package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.cache.StudentCache;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.StudentMapper;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.StudentService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_FOUND_BY_ID_MSG = "Student with id %d not found";
    private static final String STUDENT_NOT_FOUND_BY_EMAIL_MSG = "Student with email %s not found";
    private static final String EMAIL_EXISTS_MSG = "Student with email %s already exists.";
    private static final String OTHER_EMAIL_EXISTS_MSG = "Another student with email %s already exists.";

    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final StudentCache studentCache;

    private StudentService self;

    @Autowired
    @Lazy
    public void setSelf(StudentService self) {
        this.self = self;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findAllStudentsSummary() {
        List<Student> students = repository.findAll();
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDetailsDto> findAllStudentsDetails() {
        List<Student> students = repository.findAll();
        return mapper.toDetailsDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findStudentDetailsById(Long id) {
        Optional<StudentDetailsDto> cachedDto = studentCache.get(id);
        if (cachedDto.isPresent()) {
            System.out.println("==== CACHE HIT! ID: " + id + " ====");
            return cachedDto.get();
        }

        System.out.println("==== CACHE MISS! ID: " + id + " - Going to DB ====");
        StudentDetailsDto studentDto = repository.findById(id)
            .map(mapper::toDetailsDto)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));

        studentCache.put(id, studentDto);
        return studentDto;
    }

    @Override
    @Transactional
    public StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto) {
        if (repository.findStudentByEmail(studentDto.getEmail()) != null) {
            throw new IllegalArgumentException(String.format(EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }
        Student student = mapper.toEntity(studentDto);
        Student savedStudent = repository.save(student);

        StudentDetailsDto detailsDto = mapper.toDetailsDto(savedStudent);
        studentCache.put(savedStudent.getId(), detailsDto);
        return detailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findDtoByEmail(String email) {
        Student student = repository.findStudentByEmail(email);
        if (student == null) {
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return self.findStudentDetailsById(student.getId());
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
        StudentDetailsDto updatedDetailsDto = mapper.toDetailsDto(updatedStudent);
        studentCache.put(id, updatedDetailsDto);
        return updatedDetailsDto;
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id)));

        Set<Event> eventsToRemoveFrom = new HashSet<>(student.getEvents());
        for (Event event : eventsToRemoveFrom) {
            event.getStudents().remove(student);
        }
        student.getEvents().clear();

        repository.delete(student);

        studentCache.evict(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findStudentsByEventName(String eventName) {
        List<Student> students = repository.findStudentsByEventNameJpql(eventName);
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findStudentsWithActiveBarterByItem(String itemName) {
        List<Student> students = repository.findStudentsWithActiveBarterByItemNative(itemName);
        return mapper.toSummaryDtoList(students);
    }
}