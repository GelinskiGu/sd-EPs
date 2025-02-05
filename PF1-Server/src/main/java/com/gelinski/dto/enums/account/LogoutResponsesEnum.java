package com.gelinski.dto.enums.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogoutResponsesEnum {
    SUCCESSFUL_LOGOUT("010", "Successful logout"),
    FIELDS_MISSING("011", "Fields missing"),
    USER_NOT_LOGGED_IN("012", "User not logged in"),
    LOGOUT_FAILED("013", "Logout failed");

    private final String code;
    private final String message;
}
