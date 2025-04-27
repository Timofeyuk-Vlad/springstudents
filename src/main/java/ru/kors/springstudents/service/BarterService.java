package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;

import java.util.List;

public interface BarterService { // Убедись, что имя интерфейса правильное
    List<BarterDto> findAllBarters();

    BarterDto saveBarter(CreateBarterRequestDto barterDto);

    BarterDto findBarterById(Long id);

    BarterDto updateBarter(Long id, CreateBarterRequestDto barterDto);

    void deleteBarter(Long id);
}