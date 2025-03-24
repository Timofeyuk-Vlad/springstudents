package ru.kors.springstudents.service;

import ru.kors.springstudents.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> findAllRequests();

    Request saveRequest(Request request);

    Request findRequestById(Long id);

    Request updateRequest(Request request);

    void deleteRequest(Long id);
}