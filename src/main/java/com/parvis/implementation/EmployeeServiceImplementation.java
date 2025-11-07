package com.parvis.implementation;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.dto.EmployeeUpdateResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.repository.EmployeeRepository;
import com.parvis.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
            if (request == null) {
                throw new InvalidRequestException("Name is required", "NAME_MISSING");
            }
            validateRequired(request.name(), "Name", "NAME_MISSING");
            validateRequired(request.email(), "Email", "EMAIL_MISSING");
            validateRequired(request.password(), "Password", "PASSWORD_MISSING");

            if (request.shift().isEmpty()) {
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
                    ex.getHint(),
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

    @Override
    public AppResponse<EmployeeUpdateResponse> updateEmployee(EmployeeCreateRequest request) {
        try {
            if (request == null || request.empId().isEmpty()) {
                throw new InvalidRequestException("Employee details required", "EMPLOYEE_DATA_MISSING");
            }
            return employeeRepository.updateEmployee(request);
        }
        catch (DatabaseException exception) {
            return AppResponse.failure(
                    ErrorDetails.db(
                            exception.getMessage(),
                            exception.getCode(),
                            exception.getSqlState(),
                            exception.getHint(),
                            exception
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

    private void validateRequired(Optional<String> field, String fieldName, String code) {
        if (field.isEmpty() || field.get().isBlank()) {
            throw new InvalidRequestException(fieldName + " is required", code);
        }
    }

}
