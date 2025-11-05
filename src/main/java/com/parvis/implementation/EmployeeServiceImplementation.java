package com.parvis.implementation;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.repository.EmployeeRepository;
import com.parvis.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImplementation implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private static final Logger logger = Logger.getLogger(EmployeeServiceImplementation.class.getName());

    @Override
    public AppResponse<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request) {
        // Validate fields
        try {
            if (request == null || request.name() == null || request.name().isBlank()) {
                throw new InvalidRequestException("Name is required", "NAME_MISSING");
            }

            if (request.email() == null || request.email().isBlank()) {
                throw new InvalidRequestException("Email is required", "EMAIL_MISSING");
            }
            if (request.password() == null || request.password().isBlank()) {
                throw new InvalidRequestException("Password is required", "PASSWORD_MISSING");
            }
            if (request.shift() == null) {
                throw new InvalidRequestException("Shift is required", "SHIFT_MISSING");
            }

            return employeeRepository.createEmployee(request);
        }
        catch (DatabaseException ex) {
            return AppResponse.failure(
                ErrorDetails.db(
                    ex.getMessage(),
                    ex.getCode(),
                    ex.getSqlState(),
                    ex
                )
            );
        }
        catch (Exception ex) {
            return AppResponse.failure(
                ErrorDetails.service(
                    ex.getMessage(),
                    "SERVICE_UNKNOWN_ERROR",
                    ex
                )
            );
        }
    }
}
