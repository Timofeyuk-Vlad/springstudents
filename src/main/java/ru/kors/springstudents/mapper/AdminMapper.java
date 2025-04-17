package ru.kors.springstudents.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.kors.springstudents.dto.AdminDto;
// Импортируй DTO для создания/обновления, если они есть
import ru.kors.springstudents.model.Admin;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDto toDto(Admin admin);

    List<AdminDto> toDtoList(List<Admin> admins);

    Admin toEntity(AdminDto dto); // Или из CreateAdminRequestDTO

    void updateEntityFromDto(AdminDto dto, @MappingTarget Admin admin);
    // Или из UpdateAdminRequestDTO
}