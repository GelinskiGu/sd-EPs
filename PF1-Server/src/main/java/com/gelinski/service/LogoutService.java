package com.gelinski.service;

import com.gelinski.dto.enums.LogoutResponsesEnum;
import com.gelinski.dto.request.LogoutRequest;
import com.gelinski.dto.response.LogoutResponse;

import java.util.List;

public class LogoutService {
    public LogoutResponse logout(LogoutRequest request, List<String> loggedUsers) {
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
