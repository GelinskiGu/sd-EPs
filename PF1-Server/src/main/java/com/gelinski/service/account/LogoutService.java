package com.gelinski.service.account;

import com.gelinski.dto.enums.account.LogoutResponsesEnum;
import com.gelinski.dto.request.account.LogoutRequest;
import com.gelinski.dto.response.account.LogoutResponse;

import java.util.List;
import java.util.Set;

public class LogoutService {
    public LogoutResponse logout(LogoutRequest request, Set<String> loggedUsers) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            LogoutResponsesEnum fieldsMissing = LogoutResponsesEnum.FIELDS_MISSING;
            return getLogoutResponse(fieldsMissing);
        }

        if (loggedUsers.isEmpty() || !loggedUsers.contains(request.getToken())) {
            return getLogoutResponse(LogoutResponsesEnum.USER_NOT_LOGGED_IN);
        }

        loggedUsers.remove(request.getToken());
        return getLogoutResponse(LogoutResponsesEnum.SUCCESSFUL_LOGOUT);
    }

    private static LogoutResponse getLogoutResponse(LogoutResponsesEnum typeResponseEnum) {
        LogoutResponse response = new LogoutResponse();
        response.setResponse(typeResponseEnum.getCode());
        response.setMessage(typeResponseEnum.getMessage());
        return response;
    }
}
