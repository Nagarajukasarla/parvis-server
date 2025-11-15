package com.parvis.implementation;

import com.parvis.dto.EmployeeResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.repository.EmployeeRepository;
import com.parvis.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImplementation implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public AppResponse<EmployeeResponse> getEmployeeProfile(String empId) {
        try {
            if (empId == null || empId.isBlank()) {
                throw new InvalidRequestException("EmployeeId is required", "EMPLOYEE_ID_MISSING");
            }
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
}
