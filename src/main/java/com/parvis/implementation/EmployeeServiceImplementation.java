package com.parvis.implementation;

import com.parvis.dto.EmployeeAttendanceRequest;
import com.parvis.dto.EmployeePreferences;
import com.parvis.dto.EmployeeResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.repository.EmployeeRepository;
import com.parvis.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public AppResponse<EmployeeResponse> getEmployeeProfile(String empId) {
        try {
            validateRequired(Optional.of(empId), "EMPLOYEE_ID", "EMPLOYEE_ID_MISSING");
            return employeeRepository.getEmployeeProfile(empId);
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
    public AppResponse<String> markAttendance(String empId, EmployeeAttendanceRequest request) {
        try {
            validateRequired(Optional.of(empId), "EMPLOYEE_ID", "EMPLOYEE_ID_MISSING");
            return employeeRepository.markAttendance(empId, request);
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
    public AppResponse<EmployeePreferences> getEmployeePreferences() {
        try {
            return employeeRepository.getEmployeePreferences();
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
    public AppResponse<?> hasMarkedInToday(String empId) {
        try {
            validateRequired(Optional.of(empId), "EMPLOYEE_ID", "EMPLOYEE_ID_MISSING");
            return employeeRepository.hasMarkedInToday(empId);
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

    private void validateRequired(Optional<String> field, String fieldName, String code) {
        if (field.isEmpty() || field.get().isBlank()) {
            throw new InvalidRequestException(fieldName + " is required", code);
        }
    }
}
