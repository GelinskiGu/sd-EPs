package com.gelinski.dto.enums.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginResponsesEnum {
    NORMAL_USER_SUCCESSFUL_LOGIN("000", "Successful login"),
    ADMIN_USER_SUCCESSFUL_LOGIN("001", "Successful login"),
    FIELDS_MISSING("002", "Fields missing"),
    LOGIN_FAILED("003", "Login failed");

    private final String code;
    private final String message;
}
