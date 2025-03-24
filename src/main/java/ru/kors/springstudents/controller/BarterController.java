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
import ru.kors.springstudents.model.Barter;
import ru.kors.springstudents.service.BarterService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/barters")
@AllArgsConstructor
public class BarterController {
    private final BarterService service;

    @GetMapping
    public List<Barter> findAllBarters() {
        return service.findAllBarters();
    }

    @PostMapping
    public Barter saveBarter(@RequestBody Barter barter) {
        return service.saveBarter(barter);
    }

    @GetMapping("/{id}")
    public Barter findBarterById(@PathVariable Long id) {
        return service.findBarterById(id);
    }

    @PutMapping
    public Barter updateBarter(@RequestBody Barter barter) {
        return service.updateBarter(barter);
    }

    @DeleteMapping("/{id}")
    public void deleteBarter(@PathVariable Long id) {
        service.deleteBarter(id);
    }
}