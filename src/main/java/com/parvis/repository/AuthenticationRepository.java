package com.parvis.repository;

import com.parvis.dto.EmployeeLoginResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.utils.SessionUtils;
import com.parvis.utils.SqlFileLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepository extends BaseRepository {
    public AuthenticationRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    public AppResponse<EmployeeLoginResponse> validateUser(String emailOrId, String password) {
        try {
            // Step 1: Load the SQL query from an external file
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("p_email", emailOrId);
            params.addValue("p_password", password);

            AppResponse<String> result = executeQuery(SqlFileLoader.loadSqlFromFile("sql/employee/find-by-email-and-password.sql"), params, String.class);

            if (result.success()) {
                return AppResponse.success(
                        EmployeeLoginResponse.builder()
                                .empId(SessionUtils.encode(result.data()))
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
