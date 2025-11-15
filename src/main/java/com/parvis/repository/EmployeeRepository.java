package com.parvis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parvis.dto.EmployeeResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.PgErrorMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class EmployeeRepository extends BaseRepository {
    public EmployeeRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            ObjectMapper objectMapper
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, objectMapper);
    }

    private final Logger logger = Logger.getLogger(EmployeeRepository.class.getName());

    public AppResponse<EmployeeResponse> getEmployeeProfile(String empId) {
        try {
            var result = executeFunctionRecord("get_employee_profile", Map.of("p_emp_id", empId), EmployeeResponse.class);
            if (result.success()) {
                var data = result.data();
                logger.info("Response: " + data);

                return AppResponse.success(
                        data
                );
            }
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto Parsing Failed at Repository", exception);
        }
    }
}
