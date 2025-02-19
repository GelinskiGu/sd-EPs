package com.gelinski.dto.enums.announcement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteAnnouncementEnum {
    ANNOUNCEMENT_DELETED_SUCCESSFULLY("330", "Successful announcement deletion"),
    MISSING_FIELDS("331", "Missing fields"),
    INVALID_TOKEN("332", "Invalid token"),
    BAD_REQUEST("333", "Invalid information inserted"),
    UNKNOWN_ERROR("334", "Unknown error");

    private final String code;
    private final String message;
}
