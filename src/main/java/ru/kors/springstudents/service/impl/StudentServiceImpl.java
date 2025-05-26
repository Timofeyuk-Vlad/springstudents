package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.cache.StudentCache;
import ru.kors.springstudents.dto.CreateStudentRequestDto;
import ru.kors.springstudents.dto.StudentDetailsDto;
import ru.kors.springstudents.dto.StudentSummaryDto;
import ru.kors.springstudents.dto.UpdateStudentRequestDto;
import ru.kors.springstudents.exception.DuplicateResourceException;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.StudentMapper;
import ru.kors.springstudents.model.Event;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.StudentService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private static final String STUDENT_NOT_FOUND_BY_ID_MSG = "Student with id %d not found";
    private static final String STUDENT_NOT_FOUND_BY_EMAIL_MSG = "Student with email %s not found"; // Оставляем %s для String.format
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
        log.debug("Fetching summary for all students");
        List<Student> students = repository.findAll();
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDetailsDto> findAllStudentsDetails() {
        log.debug("Fetching details for all students");
        List<Student> students = repository.findAll();
        return mapper.toDetailsDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findStudentDetailsById(Long id) {
        log.debug("Attempting to find student details by ID: {}", id);
        Optional<StudentDetailsDto> cachedDto = studentCache.get(id);
        if (cachedDto.isPresent()) {
            log.info("==== CACHE HIT! ID: {} ====", id);
            return cachedDto.get();
        }

        log.info("==== CACHE MISS! ID: {} - Going to DB ====", id);
        StudentDetailsDto studentDto = repository.findById(id)
            .map(mapper::toDetailsDto)
            .orElseThrow(() -> {
                log.warn("Student not found with ID: {}", id);
                return new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id));
            });

        studentCache.put(id, studentDto);
        return studentDto;
    }

    @Override
    @Transactional
    public StudentDetailsDto saveStudent(CreateStudentRequestDto studentDto) {
        if (log.isInfoEnabled()) {
            log.info("Attempting to save new student with email: {}", Encode.forJava(studentDto.getEmail()));
        }
        if (repository.findStudentByEmail(studentDto.getEmail()) != null) {
            if (log.isWarnEnabled()) {
                log.warn("Attempt to save student with existing email: {}", Encode.forJava(studentDto.getEmail()));
            }
            throw new DuplicateResourceException(String.format(EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }
        Student student = mapper.toEntity(studentDto);
        Student savedStudent = repository.save(student);
        log.info("Student saved successfully with ID: {}", savedStudent.getId());

        StudentDetailsDto detailsDto = mapper.toDetailsDto(savedStudent);
        studentCache.put(savedStudent.getId(), detailsDto);
        log.debug("Saved student ID: {} put into cache", savedStudent.getId());
        return detailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetailsDto findDtoByEmail(String email) {
        Student student = repository.findStudentByEmail(email);
        if (student == null) {
            if (log.isWarnEnabled()) {
                log.warn("Student not found with email: {}", Encode.forJava(email));
            }
            throw new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return self.findStudentDetailsById(student.getId());
    }

    @Override
    @Transactional
    public StudentDetailsDto updateStudent(Long id, UpdateStudentRequestDto studentDto) {
        log.info("Attempting to update student with ID: {}", id);
        Student existingStudent = repository.findById(id)
            .orElseThrow(() -> {
                log.warn("Student not found for update with ID: {}", id);
                return new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id));
            });

        if (!existingStudent.getEmail().equals(studentDto.getEmail()) &&
            repository.findStudentByEmail(studentDto.getEmail()) != null) {
            if (log.isWarnEnabled()) {
                log.warn("Attempt to update student ID: {} with email {} that already exists for another student", id, Encode.forJava(studentDto.getEmail()));
            }
            throw new DuplicateResourceException(String.format(OTHER_EMAIL_EXISTS_MSG, studentDto.getEmail()));
        }

        mapper.updateEntityFromDto(studentDto, existingStudent);
        Student updatedStudent = repository.save(existingStudent);
        log.info("Student updated successfully with ID: {}", updatedStudent.getId());

        StudentDetailsDto updatedDetailsDto = mapper.toDetailsDto(updatedStudent);
        studentCache.put(id, updatedDetailsDto);
        log.debug("Updated student ID: {} put into cache", id);
        return updatedDetailsDto;
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        log.info("Attempting to delete student with ID: {}", id);
        Student student = repository.findById(id)
            .orElseThrow(() -> {
                log.warn("Student not found for deletion with ID: {}", id);
                return new ResourceNotFoundException(String.format(STUDENT_NOT_FOUND_BY_ID_MSG, id));
            });

        Set<Event> eventsToRemoveFrom = new HashSet<>(student.getEvents());
        if (!eventsToRemoveFrom.isEmpty()) {
            log.debug("Removing student ID: {} from {} events", id, eventsToRemoveFrom.size());
            for (Event event : eventsToRemoveFrom) {
                event.getStudents().remove(student);
            }
            student.getEvents().clear();
        }

        repository.delete(student);
        log.info("Student deleted successfully with ID: {}", id);

        studentCache.evict(id);
        log.debug("Student ID: {} evicted from cache", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findStudentsByEventName(String eventName) {
        if (log.isDebugEnabled()) {
            log.debug("Finding students by event name: {}", Encode.forJava(eventName));
        }
        List<Student> students = repository.findStudentsByEventNameJpql(eventName);
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentSummaryDto> findStudentsWithActiveBarterByItem(String itemName) {
        if (log.isDebugEnabled()) {
            log.debug("Finding students with active barter by item: {}", Encode.forJava(itemName));
        }
        List<Student> students = repository.findStudentsWithActiveBarterByItemNative(itemName);
        return mapper.toSummaryDtoList(students);
    }

    @Override
    @Transactional
    public List<StudentDetailsDto> saveStudentsBulk(List<CreateStudentRequestDto> studentDtos) {
        if (studentDtos == null || studentDtos.isEmpty()) {
            log.info("Received empty or null list for bulk student save.");
            return Collections.emptyList();
        }
        log.info("Attempting to save {} students in bulk.", studentDtos.size());

        Set<String> emailsInRequest = studentDtos.stream()
            .map(CreateStudentRequestDto::getEmail)
            .collect(Collectors.toSet());
        if (emailsInRequest.size() != studentDtos.size()) {
            log.warn("Duplicate emails found within the bulk request.");
            throw new DuplicateResourceException("Duplicate emails found in the bulk request.");
        }

        for (CreateStudentRequestDto dto : studentDtos) {
            if (repository.findStudentByEmail(dto.getEmail()) != null) {
                if (log.isWarnEnabled()) {
                    log.warn("Attempt to save student in bulk with existing email: {}", Encode.forJava(dto.getEmail()));
                }
                throw new DuplicateResourceException(String.format(EMAIL_EXISTS_MSG, dto.getEmail()));
            }
        }

        List<StudentDetailsDto> savedStudentDetails = studentDtos.stream()
            .map(dto -> {
                Student student = mapper.toEntity(dto);
                Student savedStudent = repository.save(student);
                StudentDetailsDto detailsDto = mapper.toDetailsDto(savedStudent);
                studentCache.put(savedStudent.getId(), detailsDto);
                log.debug("Bulk saved student ID: {} and put into cache", savedStudent.getId());
                return detailsDto;
            })
            .collect(Collectors.toList());

        log.info("Successfully saved {} students in bulk.", savedStudentDetails.size());
        return savedStudentDetails;
    }
}