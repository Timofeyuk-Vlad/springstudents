package ru.kors.springstudents.service;

import ru.kors.springstudents.model.Barter;

import java.util.List;

public interface BarterService {
    List<Barter> findAllBarters();

    Barter saveBarter(Barter barter);

    Barter findBarterById(Long id);

    Barter updateBarter(Barter barter);

    void deleteBarter(Long id);
}