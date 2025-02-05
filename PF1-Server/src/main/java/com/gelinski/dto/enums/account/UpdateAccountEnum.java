package com.gelinski.dto.enums.account;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateAccountEnum {
    SUCCESS("120", "Account successfully updated"),
    INVALID_OR_EMPTY_TOKEN("121", "Invalid or Empty Token"),
    INVALID_PERMISSION("122", "Invalid Permission, user does not have permission to update other users data"),
    USER_NOT_FOUND("123", "User not found"),
    ERROR_UPDATE_ACCOUNT("124", "Unknown error");

    private final String code;
    private final String message;
}
