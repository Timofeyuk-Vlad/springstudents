package ru.kors.springstudents.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.model.Admin;
import ru.kors.springstudents.repository.AdminRepository;
import ru.kors.springstudents.service.AdminService;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repository;

    @Override
    public List<Admin> findAllAdmins() {
        return repository.findAll();
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return repository.save(admin);
    }

    @Override
    public Admin findAdminById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return repository.save(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        repository.deleteById(id);
    }
}