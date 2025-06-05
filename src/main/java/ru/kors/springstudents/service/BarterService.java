package ru.kors.springstudents.service;

import java.util.List;
import ru.kors.springstudents.dto.BarterDto;
import ru.kors.springstudents.dto.CreateBarterRequestDto;

public interface BarterService {
    List<BarterDto> findAllBarters();

    BarterDto saveBarter(CreateBarterRequestDto barterDto);

    BarterDto findBarterById(Long id);

    BarterDto updateBarter(Long id, CreateBarterRequestDto barterDto);

    void deleteBarter(Long id);
}