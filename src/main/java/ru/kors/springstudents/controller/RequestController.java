package ru.kors.springstudents.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kors.springstudents.model.Request;
import ru.kors.springstudents.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@AllArgsConstructor
public class RequestController {
    private final RequestService service;

    @GetMapping
    public List<Request> findAllRequests() {
        return service.findAllRequests();
    }

    @PostMapping
    public Request saveRequest(@RequestBody Request request) {
        return service.saveRequest(request);
    }

    @GetMapping("/{id}")
    public Request findRequestById(@PathVariable Long id) {
        return service.findRequestById(id);
    }

    @PutMapping
    public Request updateRequest(@RequestBody Request request) {
        return service.updateRequest(request);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
    }
}