package ru.kors.springstudents.service;

import ru.kors.springstudents.dto.CreateRequestDto;
import ru.kors.springstudents.dto.RequestDto;
// import ru.kors.springstudents.dto.UpdateRequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> findAllRequests();
    
    RequestDto saveRequest(CreateRequestDto requestDto);
    
    RequestDto findRequestById(Long id);
    
    RequestDto updateRequest(Long id, CreateRequestDto requestDto);
    
    void deleteRequest(Long id);
    
}