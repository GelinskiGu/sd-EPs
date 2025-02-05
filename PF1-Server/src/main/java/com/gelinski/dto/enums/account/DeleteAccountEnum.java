package com.gelinski.dto.enums.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteAccountEnum {
    ACCOUNT_DELETED("130", "Account deleted successfully"),
    USER_NOT_FOUND("134", "User not found"),
    ERROR_DELETE_ACCOUNT("135", "Unknown error");

    private final String code;
    private final String message;
}
