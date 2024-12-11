package com.gelinski.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogoutResponsesEnum {
    SUCCESSFUL_LOGOUT("010", "Successful logout"),
    FIELDS_MISSING("011", "Fields missing");

    private final String code;
    private final String message;
}
