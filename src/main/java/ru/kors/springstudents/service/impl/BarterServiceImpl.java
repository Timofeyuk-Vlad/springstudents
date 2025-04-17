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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BarterServiceImpl implements BarterService {

    private final BarterRepository repository;
    private final StudentRepository studentRepository; // Внедряем репозиторий студентов
    private final BarterMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<BarterDto> findAllBarters() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public BarterDto saveBarter(CreateBarterRequestDto barterDto) {
        // Находим студента по ID из DTO
        Student student = studentRepository.findById(barterDto.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student with id "
                + barterDto.getStudentId()
                + " not found for Barter"));

        Barter barter = mapper.toEntity(barterDto);
        barter.setStudent(student); // Устанавливаем связь

        Barter savedBarter = repository.save(barter);
        return mapper.toDto(savedBarter);
    }

    @Override
    @Transactional(readOnly = true)
    public BarterDto findBarterById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Barter with id "
                + id + " not found"));
    }

    @Override
    public BarterDto updateBarter(Long id, CreateBarterRequestDto barterDto) { // Или UpdateDTO
        Barter existingBarter = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barter with id "
                + id + " not found"));

        // Проверяем, изменился ли студент (если это разрешено)
        if (!existingBarter.getStudent().getId().equals(barterDto.getStudentId())) {
            Student newStudent = studentRepository.findById(barterDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("New Student with id "
                    + barterDto.getStudentId() + " not found for Barter update"));
            existingBarter.setStudent(newStudent);
        }

        // Обновляем остальные поля
        mapper.updateEntityFromDto(barterDto, existingBarter);
        Barter updatedBarter = repository.save(existingBarter);
        return mapper.toDto(updatedBarter);
    }

    @Override
    public void deleteBarter(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Barter with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}