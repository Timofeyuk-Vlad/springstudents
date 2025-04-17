package ru.kors.springstudents.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springstudents.dto.AdminDto;
import ru.kors.springstudents.exception.ResourceNotFoundException;
import ru.kors.springstudents.mapper.AdminMapper;
import ru.kors.springstudents.model.Admin;
import ru.kors.springstudents.repository.AdminRepository;
import ru.kors.springstudents.service.AdminService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository repository;
    private final AdminMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdminDto> findAllAdmins() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public AdminDto saveAdmin(AdminDto adminDto) {
        Admin admin = mapper.toEntity(adminDto);
        // Здесь можно добавить логику перед сохранением, если нужно
        Admin savedAdmin = repository.save(admin);
        return mapper.toDto(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDto findAdminById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + id + " not found"));
    }

    @Override
    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        Admin existingAdmin = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + id + " not found"));
        // Обновляем существующую сущность
        mapper.updateEntityFromDto(adminDto, existingAdmin);
        Admin updatedAdmin = repository.save(existingAdmin);
        return mapper.toDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Admin with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}