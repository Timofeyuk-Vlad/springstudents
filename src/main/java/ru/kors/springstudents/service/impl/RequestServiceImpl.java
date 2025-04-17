package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateRequestDto;
import ru.kors.springstudents.dto.RequestDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.RequestMapper;
import ru.kors.springstudents.model.Request;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.RequestRepository;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final StudentRepository studentRepository;
    private final RequestMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAllRequests() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public RequestDto saveRequest(CreateRequestDto requestDto) {
        Student student = studentRepository.findById(requestDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student with id "
                + requestDto.getStudentId() + " not found for Request"));

        Request request = mapper.toEntity(requestDto);
        request.setStudent(student);
        request.setCreatedAt(LocalDateTime.now());

        Request savedRequest = repository.save(request); // Сохраняем Request
        return mapper.toDto(savedRequest); // Возвращаем RequestDto
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequestById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Request with id "
                + id + " not found"));
    }

    @Override
    public RequestDto updateRequest(Long id, CreateRequestDto requestDto) { // Или UpdateDTO
        Request existingRequest = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request with id "
                + id + " not found"));

        // Проверяем смену студента (если разрешено)
        if (!existingRequest.getStudent().getId().equals(requestDto.getStudentId())) {
            Student newStudent = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("New Student with id "
                    + requestDto.getStudentId() + " not found for Request update"));
            existingRequest.setStudent(newStudent);
        }

        mapper.updateEntityFromDto(requestDto, existingRequest);

        Request updatedRequest = repository.save(existingRequest);
        return mapper.toDto(updatedRequest);
    }

    @Override
    public void deleteRequest(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Request with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}