package ru.kors.springstudents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kors.springstudents.dto.AdminDto;
import ru.kors.springstudents.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;

    @GetMapping
    public ResponseEntity<List<AdminDto>> findAllAdmins() {
        return ResponseEntity.ok(service.findAllAdmins());
    }

    @PostMapping
    public ResponseEntity<AdminDto> saveAdmin(@RequestBody AdminDto adminDto) {
        AdminDto savedAdmin = service.saveAdmin(adminDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDto> findAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDto> updateAdmin(@PathVariable Long id,
                                                @RequestBody AdminDto adminDto) {
        return ResponseEntity.ok(service.updateAdmin(id, adminDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        service.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}