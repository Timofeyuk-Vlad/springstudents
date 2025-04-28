package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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
import ru.kors.springstudents.service.DutyService;

import java.util.HashSet; // Импорт HashSet
import java.util.List;
import java.util.Set;    // Импорт Set

@Service
@RequiredArgsConstructor
@Primary // Убери @Primary, если есть другой основной бин DutyService
public class DutyServiceImpl implements DutyService {

    private final DutyRepository repository;
    private final StudentRepository studentRepository;
    private final DutyMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DutyDto> findAllDuties() {
        List<Duty> dutiesList = repository.findAll();
        // Преобразуем List в Set
        Set<Duty> dutiesSet = new HashSet<>(dutiesList);
        return mapper.toDtoList(dutiesSet);
    }

    @Override
    @Transactional(readOnly = true)
    public DutyDto findDutyById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Duty not found with id: " + id));
    }

    @Override
    @Transactional
    public DutyDto saveDuty(CreateDutyRequestDto dutyDto) {
        Student student = studentRepository.findById(dutyDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dutyDto.getStudentId()));

        Duty duty = mapper.toEntity(dutyDto);
        duty.setStudent(student);
        Duty savedDuty = repository.save(duty);
        return mapper.toDto(savedDuty);
    }

    @Override
    @Transactional
    public DutyDto updateDuty(Long id, CreateDutyRequestDto dutyDto) {
        Duty existingDuty = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Duty not found with id: " + id));

        if (!existingDuty.getStudent().getId().equals(dutyDto.getStudentId())) {
            Student newStudent = studentRepository.findById(dutyDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dutyDto.getStudentId()));
            existingDuty.setStudent(newStudent);
        }

        mapper.updateEntityFromDto(dutyDto, existingDuty); // Метод update должен быть в маппере DutyMapper
        Duty updatedDuty = repository.save(existingDuty);
        return mapper.toDto(updatedDuty);
    }

    @Override
    @Transactional
    public void deleteDuty(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Duty not found with id: " + id);
        }
        repository.deleteById(id);
    }
}