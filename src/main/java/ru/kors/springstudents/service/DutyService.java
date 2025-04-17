package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateDutyRequestDto;
import ru.kors.springstudents.dto.DutyDto;
// import ru.kors.springstudents.dto.UpdateDutyRequestDTO;

import java.util.List;

public interface DutyService {
    List<DutyDto> findAllDuties();

    DutyDto saveDuty(CreateDutyRequestDto dutyDto);

    DutyDto findDutyById(Long id); // Бросает исключение, если не найден

    DutyDto updateDuty(Long id, CreateDutyRequestDto dutyDto); // Или UpdateDTO, бросает исключение

    void deleteDuty(Long id); // Бросает исключение, если не найден
}