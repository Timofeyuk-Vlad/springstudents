package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.Barter;
import ru.kors.springstudents.repository.BarterRepository;
import ru.kors.springstudents.service.BarterService;

import java.util.List;

@Service
@AllArgsConstructor
public class BarterServiceImpl implements BarterService {
    private final BarterRepository repository;

    @Override
    public List<Barter> findAllBarters() {
        return repository.findAll();
    }

    @Override
    public Barter saveBarter(Barter barter) {
        return repository.save(barter);
    }

    @Override
    public Barter findBarterById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Barter updateBarter(Barter barter) {
        return repository.save(barter);
    }

    @Override
    public void deleteBarter(Long id) {
        repository.deleteById(id);
    }
}