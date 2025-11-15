package com.parvis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.dto.EmployeeCreateResponse;
import com.parvis.dto.EmployeeUpdateResponse;
import com.parvis.enums.Shift;
import com.parvis.exception.DatabaseException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.PgErrorMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class AdminRepository extends BaseRepository {
    private final PasswordEncoder passwordEncoder;

    public AdminRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, objectMapper);
        this.passwordEncoder = passwordEncoder;
    }

    public AppResponse<EmployeeCreateResponse> createEmployee(EmployeeCreateRequest request) {
        try {
            AppResponse<String> result = executeFunctionScalar("add_new_employee", Map.of(
                    "p_name", request.name().orElseThrow(),
                    "p_email", request.email().orElseThrow(),
                    "p_password", passwordEncoder.encode(request.password().orElseThrow()),
                    "p_shift", request.shift().orElseThrow().getValue()
            ), String.class);

            if(result.success()) {
                return AppResponse.success(
                        EmployeeCreateResponse.builder()
                                .empId(result.data())
                                .build()
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

    public AppResponse<EmployeeUpdateResponse> updateEmployee(EmployeeCreateRequest request) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("p_emp_id", request.empId().orElse(null));
            params.put("p_email", request.email().orElse(null));
            params.put("p_name", request.name().orElse(null));
            params.put("p_password", request.password().map(passwordEncoder::encode).orElse(null));
            if (request.shift().isPresent()) params.put("p_shift", request.shift().get());
            else params.put("p_shift", null);

            AppResponse<Map<String, Object>> result = executeFunctionRecord("update_employee", params);

            if (result.success()) {
                Map<String, Object> data = result.data();
                return AppResponse.success(
                        EmployeeUpdateResponse.builder()
                                .empId((String) data.get("emp_id"))
                                .name((String) data.get("name"))
                                .email((String) data.get("email"))
                                .shift(Objects.equals(data.get("shift"), "day") ? Shift.DAY : Shift.NIGHT)
                                .build()
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

    public AppResponse<String> deleteEmployee(String id) {
        try {
            var result = executeFunctionScalar("delete_employee", Map.of("p_emp_id", id), String.class);
            if (result.success()) {
                return AppResponse.success(result.data());
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
