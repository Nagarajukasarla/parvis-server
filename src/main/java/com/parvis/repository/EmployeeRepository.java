package com.parvis.repository;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class EmployeeRepository extends BaseRepository {
    public EmployeeRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    public AppResponse<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request) {
        try {
            AppResponse<String> result = executeFunctionScalar("add_new_employee", Map.of(
                    "p_name", request.name(),
                    "p_email", request.email(),
                    "p_password", request.password(),
                    "p_shift", request.shift().getValue()
            ), String.class);

            if(result.success()) {
                return AppResponse.success(
                        EmployeeCreateResponse.builder()
                                .empId(result.data())
                                .build()
                );
            }
            else {
                var error = result.errorDetails();
                throw switch (error.origin()) {
                    case DATABASE -> new DatabaseException(error.message(), error.code(), error.sqlState(), error.cause());
                    case SERVICE, CONTROLLER, REPOSITORY -> new InvalidRequestException(error.message(), error.code());
                };
            }
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto Parsing Failed at Repository", exception);
        }
    }
}
