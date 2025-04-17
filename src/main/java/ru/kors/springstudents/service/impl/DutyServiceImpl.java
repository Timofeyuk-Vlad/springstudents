package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.CreateDutyRequestDto;
import ru.kors.springstudents.dto.DutyDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.DutyMapper;
import ru.kors.springstudents.model.Duty;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.DutyRepository;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.DutyService; // Убедись, что интерфейс обновлен

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DutyServiceImpl implements DutyService {

    private final DutyRepository repository;
    private final StudentRepository studentRepository;
    private final DutyMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DutyDto> findAllDuties() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public DutyDto saveDuty(CreateDutyRequestDto dutyDto) {
        Student student = studentRepository.findById(dutyDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student with id "
                + dutyDto.getStudentId() + " not found for Duty"));

        Duty duty = mapper.toEntity(dutyDto);
        duty.setStudent(student);

        Duty savedDuty = repository.save(duty);
        return mapper.toDto(savedDuty);
    }

    @Override
    @Transactional(readOnly = true)
    public DutyDto findDutyById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Duty with id " + id + " not found"));
    }

    @Override
    public DutyDto updateDuty(Long id, CreateDutyRequestDto dutyDto) { // Или UpdateDTO
        Duty existingDuty = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Duty with id " + id + " not found"));

        // Проверка смены студента
        if (!existingDuty.getStudent().getId().equals(dutyDto.getStudentId())) {
            Student newStudent = studentRepository.findById(dutyDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("New Student with id "
                    + dutyDto.getStudentId() + " not found for Duty update"));
            existingDuty.setStudent(newStudent);
        }

        mapper.updateEntityFromDto(dutyDto, existingDuty); // Обновляем дату
        Duty updatedDuty = repository.save(existingDuty);
        return mapper.toDto(updatedDuty);
    }

    @Override
    public void deleteDuty(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Duty with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}