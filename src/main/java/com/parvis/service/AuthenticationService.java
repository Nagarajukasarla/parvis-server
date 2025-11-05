package com.parvis.service;

import com.parvis.dto.EmployeeLoginRequest;
import com.parvis.dto.EmployeeLoginResponse;
import com.parvis.factory.AppResponse;

public interface AuthenticationService {
    AppResponse<EmployeeLoginResponse> validateUser(EmployeeLoginRequest request);
}
