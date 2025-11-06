package com.parvis.implementation;

import com.parvis.dto.EmployeeLoginRequest;
import com.parvis.dto.EmployeeLoginResponse;
import com.parvis.exception.DatabaseException;
import com.parvis.exception.InvalidRequestException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.repository.AuthenticationRepository;
import com.parvis.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final AuthenticationRepository authRepository;
    //private final Logger logger = Logger.getLogger(AuthenticationServiceImplementation.class.getName());

    @Override
    public AppResponse<EmployeeLoginResponse> validateUser(EmployeeLoginRequest request) {
        try {
            if (request == null || request.emailOrId() == null || request.emailOrId().isBlank()) {
                throw new InvalidRequestException("Email or Id is required", "EMAIL_OR_ID_MISSING");
            }
            if (request.password() == null || request.password().isBlank()) {
                throw new InvalidRequestException("Password is required", "PASSWORD_MISSING");
            }
            return authRepository.validateUser(request.emailOrId(), request.password());
        }
        catch (DatabaseException exception) {
            return AppResponse.failure(
                    ErrorDetails.db(
                            exception.getMessage(),
                            exception.getCode(),
                            exception.getSqlState(),
                            exception.getHint(),
                            exception
                    )
            );
        }
        catch (Exception exception) {
            return AppResponse.failure(
                    ErrorDetails.service(
                            exception.getMessage(),
                            "SERVICE_UNKNOWN_ERROR",
                            exception
                    )
            );
        }
    }
}
