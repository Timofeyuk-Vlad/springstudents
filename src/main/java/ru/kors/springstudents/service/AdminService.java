package ru.kors.springstudents.service;

import ru.kors.springstudents.model.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> findAllAdmins();

    Admin saveAdmin(Admin admin);

    Admin findAdminById(Long id);

    Admin updateAdmin(Admin admin);

    void deleteAdmin(Long id);
}