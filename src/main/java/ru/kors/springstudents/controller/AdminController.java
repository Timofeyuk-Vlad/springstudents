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
import ru.kors.springstudents.model.Admin;
import ru.kors.springstudents.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@AllArgsConstructor
public class AdminController {
    private final AdminService service;

    @GetMapping
    public List<Admin> findAllAdmins() {
        return service.findAllAdmins();
    }

    @PostMapping
    public Admin saveAdmin(@RequestBody Admin admin) {
        return service.saveAdmin(admin);
    }

    @GetMapping("/{id}")
    public Admin findAdminById(@PathVariable Long id) {
        return service.findAdminById(id);
    }

    @PutMapping
    public Admin updateAdmin(@RequestBody Admin admin) {
        return service.updateAdmin(admin);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        service.deleteAdmin(id);
    }
}