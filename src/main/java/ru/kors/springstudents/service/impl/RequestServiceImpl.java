package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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

import java.time.LocalDateTime; // Импорт для установки времени
import java.util.HashSet; // Импорт HashSet
import java.util.List;
import java.util.Set;    // Импорт Set

@Service
@RequiredArgsConstructor
@Primary // Убери @Primary, если есть другой основной бин RequestService
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final StudentRepository studentRepository;
    private final RequestMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> findAllRequests() {
        List<Request> requestList = repository.findAll();
        // Преобразуем List в Set
        Set<Request> requestSet = new HashSet<>(requestList);
        return mapper.toDtoList(requestSet);
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto findRequestById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
    }

    @Override
    @Transactional
    public RequestDto saveRequest(CreateRequestDto requestDto) {
        Student student = studentRepository.findById(requestDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + requestDto.getStudentId()));

        Request request = mapper.toEntity(requestDto);
        request.setStudent(student);
        request.setCreatedAt(LocalDateTime.now()); // Устанавливаем время создания
        Request savedRequest = repository.save(request);
        return mapper.toDto(savedRequest);
    }

    @Override
    @Transactional
    public RequestDto updateRequest(Long id, CreateRequestDto requestDto) {
        Request existingRequest = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));

        if (!existingRequest.getStudent().getId().equals(requestDto.getStudentId())) {
            Student newStudent = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + requestDto.getStudentId()));
            existingRequest.setStudent(newStudent);
        }

        mapper.updateEntityFromDto(requestDto, existingRequest); // Метод должен быть в RequestMapper
        // createdAt не обновляем?
        Request updatedRequest = repository.save(existingRequest);
        return mapper.toDto(updatedRequest);
    }

    @Override
    @Transactional
    public void deleteRequest(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Request not found with id: " + id);
        }
        repository.deleteById(id);
    }
}