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
import ru.kors.springstudents.model.Duty;
import ru.kors.springstudents.service.DutyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/duties")
@AllArgsConstructor
public class DutyController {
    private final DutyService service;

    @GetMapping
    public List<Duty> findAllDuties() {
        return service.findAllDuties();
    }

    @PostMapping
    public Duty saveDuty(@RequestBody Duty duty) {
        return service.saveDuty(duty);
    }

    @GetMapping("/{id}")
    public Duty findDutyById(@PathVariable Long id) {
        return service.findDutyById(id);
    }

    @PutMapping
    public Duty updateDuty(@RequestBody Duty duty) {
        return service.updateDuty(duty);
    }

    @DeleteMapping("/{id}")
    public void deleteDuty(@PathVariable Long id) {
        service.deleteDuty(id);
    }
}