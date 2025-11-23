package com.parvis.factory;

import com.parvis.exception.*;

import java.util.Map;
import java.util.function.Function;

public final class PgErrorMapper {
    private static final Map<String, Function<ErrorDetails, DatabaseException>> SQLSTATE_MAPPINGS = Map.of(
            "E0010", e -> new UserNotFoundException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause()),
            "E0014", e -> new ShiftNotFoundException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause()),
            "E0021", e -> new NoOpenPunchExistsException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause()),
            "E0022",  e -> new AttendanceAlreadyMarkedException(e.message(), e.code(), e.sqlState(), e.hint(), e.cause())
    );

    private PgErrorMapper() {}

    public static DatabaseException map(ErrorDetails error) {
        if (error.sqlState() != null && SQLSTATE_MAPPINGS.containsKey(error.sqlState())) {
            return SQLSTATE_MAPPINGS.get(error.sqlState()).apply(error);
        }

        String msg = error.message() != null ? error.message().toLowerCase() : "";

        if (msg.contains("employee not found")) {
            return new UserNotFoundException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }
        if (msg.contains("shift not found")) {
            return new ShiftNotFoundException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }
        if (msg.contains("already exists")) {
            return new UserAlreadyExistsException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }
        if (msg.contains("no open")) {
            return new NoOpenPunchExistsException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
        }

        return new DatabaseException(error.message(), error.code(), error.sqlState(), error.hint(), error.cause());
    }
}
