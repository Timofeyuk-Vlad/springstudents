package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.AdminDto; // Используем DTO

import java.util.List;

public interface AdminService {

    List<AdminDto> findAllAdmins();

    AdminDto saveAdmin(AdminDto adminDto); // Принимаем DTO (или CreateAdminDto)

    AdminDto findAdminById(Long id);

    AdminDto updateAdmin(Long id, AdminDto adminDto); // Принимаем ID и DTO (или UpdateAdminDto)

    void deleteAdmin(Long id);
}