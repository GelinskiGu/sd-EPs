package com.gelinski.service;

import com.gelinski.dto.enums.LogoutResponsesEnum;
import com.gelinski.dto.request.LogoutRequest;
import com.gelinski.dto.response.LogoutResponse;

public class LogoutService {
    public LogoutResponse logout(LogoutRequest request) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            LogoutResponsesEnum fieldsMissing = LogoutResponsesEnum.FIELDS_MISSING;
            return getLogoutResponse(fieldsMissing);
        }

        return getLogoutResponse(LogoutResponsesEnum.SUCCESSFUL_LOGOUT);
    }

    private static LogoutResponse getLogoutResponse(LogoutResponsesEnum typeResponseEnum) {
        LogoutResponse response = new LogoutResponse();
        response.setCode(typeResponseEnum.getCode());
        response.setMessage(typeResponseEnum.getMessage());
        return response;
    }
}
