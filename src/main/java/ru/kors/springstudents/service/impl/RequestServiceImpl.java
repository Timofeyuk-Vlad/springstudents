package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.Request;
import ru.kors.springstudents.repository.RequestRepository;
import ru.kors.springstudents.service.RequestService;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;

    @Override
    public List<Request> findAllRequests() {
        return repository.findAll();
    }

    @Override
    public Request saveRequest(Request request) {
        return repository.save(request);
    }

    @Override
    public Request findRequestById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Request updateRequest(Request request) {
        return repository.save(request);
    }

    @Override
    public void deleteRequest(Long id) {
        repository.deleteById(id);
    }
}