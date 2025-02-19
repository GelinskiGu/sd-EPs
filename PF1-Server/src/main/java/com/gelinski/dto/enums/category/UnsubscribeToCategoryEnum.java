package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnsubscribeToCategoryEnum {
    UNSUBSCRIBE_SUCCESSFUL("350", "Successfully unsubscribed"),
    MISSING_FIELDS("351", "Missing fields"),
    INVALID_TOKEN("352", "Invalid token"),
    BAD_REQUEST("353", "Invalid information inserted"),
    UNKNOWN_ERROR("354", "Unknown error");

    private final String code;
    private final String message;
}
