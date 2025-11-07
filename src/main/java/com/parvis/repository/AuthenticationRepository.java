package com.parvis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parvis.dto.EmployeeLoginResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidPasswordException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.PgErrorMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class AuthenticationRepository extends BaseRepository {
    private final PasswordEncoder passwordEncoder;

    public AuthenticationRepository (
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper
    ) {
        super(jdbcTemplate, namedParameterJdbcTemplate, objectMapper);
        this.passwordEncoder = passwordEncoder;
    }

    private final Logger logger = Logger.getLogger(AuthenticationRepository.class.getName());

    public AppResponse<EmployeeLoginResponse> validateUser(String emailOrId, String password) {
        try {
            AppResponse<Map<String, Object>> result = executeFunctionRecord("get_employee_for_auth", Map.of(
                    "p_email_or_id", emailOrId
            ));

            if (result.success()) {
                if (!passwordEncoder.matches(password, (String) result.data().get("password_hash"))) {
                    throw new InvalidPasswordException("Invalid Password for " + emailOrId, "PASSWORD_MISMATCH");
                }
                return AppResponse.success(
                        EmployeeLoginResponse.builder()
                                .empId((String) result.data().get("emp_id"))
                                .build()
                );
            }
            throw PgErrorMapper.map(result.errorDetails());
        }
        catch (DatabaseException exception) {
            throw exception;
        }
        catch (Exception exception) {
            if (exception instanceof InvalidPasswordException) {
                throw exception;
            }
            throw new RuntimeException("Dto Parsing Failed at Repository", exception);
        }
    }
}
