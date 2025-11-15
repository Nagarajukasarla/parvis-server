package com.parvis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Base repository class providing common database operations and utility methods.
 * This abstract class serves as a foundation for repository implementations,
 * offering methods for executing queries and stored procedures with proper error handling.
 */
@RequiredArgsConstructor
public abstract class BaseRepository {
    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final Map<String, SimpleJdbcCall> functionCache = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;
    private Logger logger = Logger.getLogger(BaseRepository.class.getName());

    /**
     * Retrieves a SimpleJdbcCall instance for the specified function name.
     * If the function is not already in the cache, a new instance is created and cached.
     *
     * @param functionName the name of the database function
     * @return SimpleJdbcCall instance configured for the specified function
     * @throws IllegalArgumentException if functionName is null or empty
     */
    protected SimpleJdbcCall getFunction(String functionName) {
        return functionCache.computeIfAbsent(functionName, name ->
                new SimpleJdbcCall(jdbcTemplate).withFunctionName(name)
        );
    }

    /**
     * Executes a parameterized SQL query and returns a single result.
     *
     * @param <T> the expected type of the result
     * @param sql the SQL query to execute
     * @param params the parameters to bind to the query
     * @param returnType the class of the expected return type
     * @return AppResponse containing the query result or error details
     * @throws IllegalArgumentException if sql or returnType is null
     */
    protected <T> AppResponse<T> executeQuery(String sql, MapSqlParameterSource params, Class<T> returnType) {
        try {
            T result = namedParameterJdbcTemplate.queryForObject(sql, params, returnType);
            return AppResponse.success(result);
        }
        catch (DataAccessException exception) {
            return AppResponse.failure(
                    extractSqlErrorDetails(
                            exception,
                            "Database Query Failed: " + sql,
                            "DB_QUERY_ERROR"
                    )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.repository(
                            "Database Query error: " + sql,
                            "DB_QUERY_ERROR_UNKNOWN",
                            exception
                    )
            );
        }
    }

    /**
     * Executes a parameterized SQL query and returns a list of results.
     *
     * @param <T> the type of objects in the result list
     * @param sql the SQL query to execute
     * @param params the parameters to bind to the query
     * @param mapper the RowMapper implementation to map each row to a result object
     * @return AppResponse containing the list of results or error details
     * @throws IllegalArgumentException if sql or mapper is null
     */
    protected <T> AppResponse<List<T>> executeQueryList(String sql, MapSqlParameterSource params, RowMapper<T> mapper) {
        try {
            List<T> result = namedParameterJdbcTemplate.query(sql, params, mapper);
            return AppResponse.success(result);
        }
        catch (DataAccessException exception) {
            return AppResponse.failure(
                    extractSqlErrorDetails(
                            exception,
                            "Database Query Failed: " + sql,
                            "DB_QUERY_ERROR"
                    )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.repository(
                            "Database Query error: " + sql,
                            "DB_QUERY_ERROR_UNKNOWN",
                            exception
                    )
            );
        }
    }

    /**
     * Executes a database function that returns a single scalar value.
     *
     * @param <T> The expected return type of the function
     * @param name The name of the database function to execute
     * @param params A map of parameter names to their values for the function call
     * @param returnType The expected class of the return value
     * @return An AppResponse containing the function result on success, or error details on failure
     * @throws NullPointerException if any of the parameters are null
     */
    protected <T> AppResponse<T> executeFunctionScalar(String name, Map<String, Object> params, Class<T> returnType) {
        try {
            SimpleJdbcCall call = getFunction(name);
            T result = call.executeFunction(returnType, params);
            return AppResponse.success(result);
        }
        catch (DataAccessException exception) {
            return AppResponse.failure(
                    extractSqlErrorDetails(
                            exception,
                            "Database function failed: " + name,
                            "DB_FUNCTION_ERROR"
                    )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.repository(
                            "Unexpected error while executing function: " + name,
                            "REPOSITORY_FUNCTION_ERROR",
                            exception
                    )
            );
        }
    }

    /**
     * Executes a database function that returns a record as a map of column names to values.
     *
     * @param name the name of the database function to execute
     * @param params a map of parameter names to their values for the function call
     * @return AppResponse containing the function result as a map or error details
     * @throws IllegalArgumentException if name is null or empty
     * @throws NullPointerException if params is null
     */
    protected AppResponse<Map<String, Object>> executeFunctionRecord(String name, Map<String, Object> params) {
        try {
            var call = getFunction(name);

            Object out = call.executeFunction(Object.class, params);

            String json;
            if (out instanceof PGobject) {
                json = ((PGobject) out).getValue();
            } else if (out != null) {
                json = out.toString();
            } else {
                json = null;
            }

            if (json == null) {
                return AppResponse.failure(
                        ErrorDetails.repository("Function returned null: " + name, "DB_FUNCTION_NULL", null)
                );
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.readValue(json, Map.class);

            return AppResponse.success(result);
        }
        catch (DataAccessException exception) {
            return AppResponse.failure(
                    extractSqlErrorDetails(
                    exception,
                    "Database function failed: " + name,
                    "DB_FUNCTION_ERROR"
                )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.repository(
                            "Unexpected function error: " + name,
                            "DB_FUNCTION_ERROR_UNKNOWN",
                            exception
                )
            );
        }
    }

    protected <T> AppResponse<T> executeFunctionRecord(String name, Map<String, Object> params, Class<T> returnType) {
        try {
            var call = getFunction(name);

            Object out = call.executeFunction(Object.class, params);

            String json;
            if (out instanceof PGobject) {
                json = ((PGobject) out).getValue();
            } else if (out != null) {
                json = out.toString();
            } else {
                json = null;
            }

            if (json == null) {
                return AppResponse.failure(
                        ErrorDetails.repository("Function returned null: " + name, "DB_FUNCTION_NULL", null)
                );
            }

            T result = objectMapper.readValue(json, returnType);

            return AppResponse.success(result);
        }
        catch (DataAccessException exception) {
            return AppResponse.failure(
                    extractSqlErrorDetails(
                            exception,
                            "Database function failed: " + name,
                            "DB_FUNCTION_ERROR"
                    )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.repository(
                            "Unexpected function error: " + name,
                            "DB_FUNCTION_ERROR_UNKNOWN",
                            exception
                    )
            );
        }
    }

    /*
    *  Define executeFunction()
    *  Make sure it should be customized to handle conditions as well
    *
    * */
    protected <T> AppResponse<T> executeFunctionListRecords() {
        return null;
    }

    private ErrorDetails extractSqlErrorDetails(DataAccessException ex, String contextMessage, String code) {
        Throwable cause = ex.getRootCause();

        String sqlState = null;
        String message = ex.getMessage();
        String hint = null;

        if (cause instanceof SQLException sqlEx) {
            sqlState = sqlEx.getSQLState();
            message = sqlEx.getMessage();
            hint = extractPgHint(sqlEx);
        }

        return ErrorDetails.db(
                message != null ? message : contextMessage,
                code,
                sqlState,
                hint,
                cause
        );
    }

    private String extractPgHint(SQLException sqlEx) {
        String msg = sqlEx.getMessage();
        if (msg == null) return null;

        int idx = msg.indexOf("Hint:");
        if (idx != -1) {
            return msg.substring(idx).trim().split("\n")[0];
        }
        return null;
    }
}
