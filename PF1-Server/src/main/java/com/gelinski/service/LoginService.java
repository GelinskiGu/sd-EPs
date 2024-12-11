package com.gelinski.service;

import com.gelinski.dto.enums.LoginResponsesEnum;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.response.LoginResponse;
import com.gelinski.entity.Account;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginService {
    private final DatabaseService databaseService;

    public LoginResponse login(LoginRequest request) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            LoginResponsesEnum fieldsMissing = LoginResponsesEnum.FIELDS_MISSING;
            return getLoginResponse(fieldsMissing);
        }

        try {
            String token = authenticateUser(request);
            LoginResponsesEnum normalUserSuccessfulLogin = LoginResponsesEnum.NORMAL_USER_SUCCESSFUL_LOGIN;
            LoginResponse response = getLoginResponse(normalUserSuccessfulLogin);
            response.setToken(token);
            return response;
        } catch (Exception e) {
            LoginResponsesEnum loginFailed = LoginResponsesEnum.LOGIN_FAILED;
            return getLoginResponse(loginFailed);
        }
    }

    private static LoginResponse getLoginResponse(LoginResponsesEnum normalUserSuccessfulLogin) {
        LoginResponse response = new LoginResponse();
        response.setCode(normalUserSuccessfulLogin.getCode());
        response.setMessage(normalUserSuccessfulLogin.getMessage());
        return response;
    }

    private String authenticateUser(LoginRequest request) {
        Optional<Account> optionalUser = databaseService.findByUser(request.getUser());
        if (optionalUser.isEmpty() || !Objects.equals(optionalUser.get().getPassword(), request.getPassword())) {
            throw new RuntimeException("User not found or password incorrect");
        }

        return optionalUser.get().getUser();
    }
}
