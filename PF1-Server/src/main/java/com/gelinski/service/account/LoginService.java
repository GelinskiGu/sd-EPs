package com.gelinski.service.account;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.account.LoginResponsesEnum;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.response.account.LoginResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginService {
    public LoginResponse login(LoginRequest request) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            LoginResponsesEnum fieldsMissing = LoginResponsesEnum.FIELDS_MISSING;
            return getLoginResponse(fieldsMissing);
        }

        try {
            Account account = authenticateUser(request);
            if (Boolean.TRUE.equals(account.getIsAdmin())) {
                LoginResponsesEnum adminSuccessfulLogin = LoginResponsesEnum.ADMIN_USER_SUCCESSFUL_LOGIN;
                LoginResponse loginResponse = getLoginResponse(adminSuccessfulLogin);
                loginResponse.setToken(account.getUser());
                return loginResponse;
            }
            LoginResponsesEnum normalUserSuccessfulLogin = LoginResponsesEnum.NORMAL_USER_SUCCESSFUL_LOGIN;
            LoginResponse response = getLoginResponse(normalUserSuccessfulLogin);
            response.setToken(account.getUser());
            return response;
        } catch (Exception e) {
            LoginResponsesEnum loginFailed = LoginResponsesEnum.LOGIN_FAILED;
            return getLoginResponse(loginFailed);
        }
    }

    private static LoginResponse getLoginResponse(LoginResponsesEnum normalUserSuccessfulLogin) {
        LoginResponse response = new LoginResponse();
        response.setResponse(normalUserSuccessfulLogin.getCode());
        response.setMessage(normalUserSuccessfulLogin.getMessage());
        return response;
    }

    private Account authenticateUser(LoginRequest request) {
        Optional<Account> optionalUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            optionalUser = accountRepository.getByUser(request.getUser());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (optionalUser.isEmpty() || !Objects.equals(optionalUser.get().getPassword(), request.getPassword())) {
            throw new RuntimeException("User not found or password incorrect");
        }

        return optionalUser.get();
    }
}
