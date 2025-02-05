package com.gelinski.dto.enums.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReadAccountEnum {
    SUCCESSFUL_READ_ACCOUNT_NORMAL_USER("110", "Returns all information of the account"),
    SUCCESSFUL_READ_ACCOUNT_ADMIN_USER("111", "Returns all information of the account"),
    INVALID_OR_EMPTY_TOKEN("112", "Invalid or empty token"),
    INVALID_PERMISSION("113", "Invalid Permission, user does not have permission to visualize other users data"),
    USER_NOT_FOUND("114", "User not found"),
    ERROR_READ_ACCOUNT("115", "Unknown error");

    private final String code;
    private final String message;
}
