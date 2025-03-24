package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.Duty;
import ru.kors.springstudents.repository.DutyRepository;
import ru.kors.springstudents.service.DutyService;

import java.util.List;

@Service
@AllArgsConstructor
public class DutyServiceImpl implements DutyService {
    private final DutyRepository repository;

    @Override
    public List<Duty> findAllDuties() {
        return repository.findAll();
    }

    @Override
    public Duty saveDuty(Duty duty) {
        return repository.save(duty);
    }

    @Override
    public Duty findDutyById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Duty updateDuty(Duty duty) {
        return repository.save(duty);
    }

    @Override
    public void deleteDuty(Long id) {
        repository.deleteById(id);
    }
}