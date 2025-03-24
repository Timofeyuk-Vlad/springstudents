package ru.kors.springstudents.service;

import ru.kors.springstudents.model.Duty;

import java.util.List;

public interface DutyService {
    List<Duty> findAllDuties();

    Duty saveDuty(Duty duty);

    Duty findDutyById(Long id);

    Duty updateDuty(Duty duty);

    void deleteDuty(Long id);
}