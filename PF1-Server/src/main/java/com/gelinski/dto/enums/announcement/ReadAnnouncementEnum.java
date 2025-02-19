package com.gelinski.dto.enums.announcement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReadAnnouncementEnum {
    ANNOUNCEMENT_READ_SUCCESSFULLY("310", "Successful announcement read"),
    MISSING_FIELDS("311", "Missing fields"),
    INVALID_TOKEN("312", "Invalid token"),
    UNKNOWN_ERROR("314", "Unknown error");

    private final String code;
    private final String message;
}
