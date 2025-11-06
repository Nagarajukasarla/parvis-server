package com.parvis.factory;

import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidPasswordException;
import com.parvis.exception.UserNotFoundException;

import java.util.Map;
import java.util.function.Function;

public final class PgErrorMapper {
    private static final Map<String, Function<ErrorDetails, DatabaseException>> SQLSTATE_MAPPINGS = Map.of(
            "E0010", e -> new UserNotFoundException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause()),
            "E0011", e -> new InvalidPasswordException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause())
    );

    private PgErrorMapper() {}

    public static DatabaseException map(ErrorDetails error) {
        if (error.sqlState() != null && SQLSTATE_MAPPINGS.containsKey(error.sqlState())) {
            return SQLSTATE_MAPPINGS.get(error.sqlState()).apply(error);
        }

        String msg = error.message() != null ? error.message().toLowerCase() : "";

        if (msg.contains("not found")) {
            return new UserNotFoundException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }
        if (msg.contains("invalid password")) {
            return new InvalidPasswordException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }

        return new DatabaseException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
    }
}
