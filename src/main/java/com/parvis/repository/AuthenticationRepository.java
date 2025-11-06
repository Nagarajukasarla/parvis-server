package com.parvis.repository;

import com.parvis.dto.EmployeeLoginResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidPasswordException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.exception.UserNotFoundException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.PgErrorMapper;
import com.parvis.utils.SessionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.logging.Logger;

@Repository
public class AuthenticationRepository extends BaseRepository {
    public AuthenticationRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    private final Logger logger = Logger.getLogger(AuthenticationRepository.class.getName());

    public AppResponse<EmployeeLoginResponse> validateUser(String emailOrId, String password) {
        try {

            AppResponse<String> result = executeFunctionScalar("authenticate", Map.of(
                    "p_email_or_id", emailOrId,
                    "p_password", password
            ), String.class);

            if (result.success()) {
                return AppResponse.success(
                        EmployeeLoginResponse.builder()
                                .empId(SessionUtils.encode(result.data()))
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
}
