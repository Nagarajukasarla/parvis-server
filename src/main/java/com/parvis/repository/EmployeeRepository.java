package com.parvis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parvis.dto.EmployeeAttendanceRequest;
import com.parvis.dto.EmployeePreferences;
import com.parvis.dto.EmployeeResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.PgErrorMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
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
            if (result.success()) return AppResponse.success(result.data());
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto Parsing Failed at Repository", exception);
        }
    }

    public AppResponse<String> markAttendance(String empId, EmployeeAttendanceRequest request) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("p_emp_id", empId);
            params.put("p_in_time",
                    request.inTime() != null
                            ? Timestamp.from(request.inTime().toInstant())
                            : null
            );

            params.put("p_out_time",
                    request.outTime() != null
                            ? Timestamp.from(request.outTime().toInstant())
                            : null
            );

            var result = executeFunctionScalar("mark_attendance", params, Boolean.class);
            if (result.success()) {
                return  AppResponse.success("Marked Successfully");
            }
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto parsing failed at Repository", exception);
        }
    }

    public AppResponse<EmployeePreferences> getEmployeePreferences() {
        try {
            var result = executeFunctionRecord("get_preferences_for_employee", new HashMap<>(), EmployeePreferences.class);
            if (result.success()) {
                return AppResponse.success(result.data());
            }
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto parsing failed at Repository", exception);
        }
    }

    public AppResponse<?> hasMarkedInToday(String empId) {
        try {
            var result = executeFunctionRecord("has_marked_in_today", Map.of("p_emp_id", empId), Boolean.class);
            if (result.success()) {
                return AppResponse.success(result.data());
            }
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            throw new RuntimeException("Dto parsing failed at Repository", exception);
        }
    }
}
