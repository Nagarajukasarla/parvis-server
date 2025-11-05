package com.parvis.service;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.factory.AppResponse;

public interface EmployeeService {
    AppResponse<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request);
}