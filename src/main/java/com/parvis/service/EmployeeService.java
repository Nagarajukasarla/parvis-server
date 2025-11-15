package com.parvis.service;

import com.parvis.dto.EmployeeResponse;
import com.parvis.factory.AppResponse;

public interface EmployeeService {

    AppResponse<EmployeeResponse> getEmployeeProfile(String empId);
}
