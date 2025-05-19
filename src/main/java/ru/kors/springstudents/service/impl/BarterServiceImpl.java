package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.BarterMapper;
import ru.kors.springstudents.model.Barter;
import ru.kors.springstudents.model.Student;
import ru.kors.springstudents.repository.BarterRepository;
import ru.kors.springstudents.repository.StudentRepository;
import ru.kors.springstudents.service.BarterService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BarterServiceImpl implements BarterService {

    private final BarterRepository repository;
    private final StudentRepository studentRepository;
    private final BarterMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<BarterDto> findAllBarters() {
        List<Barter> bartersList = repository.findAll();
        Set<Barter> bartersSet = new HashSet<>(bartersList);
        return mapper.toDtoList(bartersSet);
    }

    @Override
    @Transactional(readOnly = true)
    public BarterDto findBarterById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Barter not found with id: " + id));
    }

    @Override
    @Transactional
    public BarterDto saveBarter(CreateBarterRequestDto barterDto) {
        // Находим студента
        Student student = studentRepository.findById(barterDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + barterDto.getStudentId()));

        Barter barter = mapper.toEntity(barterDto);
        barter.setStudent(student); // Устанавливаем связь
        Barter savedBarter = repository.save(barter);
        return mapper.toDto(savedBarter);
    }

    @Override
    @Transactional
    public BarterDto updateBarter(Long id, CreateBarterRequestDto barterDto) {
        Barter existingBarter = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barter not found with id: " + id));

        if (!existingBarter.getStudent().getId().equals(barterDto.getStudentId())) {
            Student newStudent = studentRepository.findById(barterDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + barterDto.getStudentId()));
            existingBarter.setStudent(newStudent);
        }

        mapper.updateEntityFromDto(barterDto, existingBarter);
        Barter updatedBarter = repository.save(existingBarter);
        return mapper.toDto(updatedBarter);
    }

    @Override
    @Transactional
    public void deleteBarter(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Barter not found with id: " + id);
        }
        repository.deleteById(id);
    }
}