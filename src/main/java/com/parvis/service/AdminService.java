package com.parvis.service;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.dto.EmployeeUpdateResponse;
import com.parvis.factory.AppResponse;

public interface AdminService {
    AppResponse<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request);

    AppResponse<EmployeeUpdateResponse> updateEmployee(EmployeeCreateRequest request);

    AppResponse<String> deleteEmployee(String id);
}