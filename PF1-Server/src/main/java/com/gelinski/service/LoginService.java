package com.gelinski.service;

import com.gelinski.dto.enums.LoginResponsesEnum;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.response.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest request) {
        if (request.fieldsMissing()) {
            LoginResponsesEnum fieldsMissing = LoginResponsesEnum.FIELDS_MISSING;
            return getLoginResponse(fieldsMissing);
        }

        try {
            LoginResponsesEnum normalUserSuccessfulLogin = LoginResponsesEnum.NORMAL_USER_SUCCESSFUL_LOGIN;
            return getLoginResponse(normalUserSuccessfulLogin);
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

    private boolean authenticateUser() {
        return true;
    }
}
