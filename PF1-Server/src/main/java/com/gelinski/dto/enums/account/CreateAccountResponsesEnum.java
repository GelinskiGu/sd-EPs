package com.gelinski.dto.enums.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CreateAccountResponsesEnum {
    SUCCESSFUL_ACCOUNT_CREATION("100", "Successful account creation"),
    FIELDS_MISSING("101", "Fields missing"),
    INVALID_INFORMATION("102", "Invalid information inserted: user or password"),
    USER_ALREADY_EXISTS("103", "Already exists an account with the username"),
    ERROR_CREATE_ACCOUNT("104", "Unknown error");

    private final String code;
    private final String message;
}
