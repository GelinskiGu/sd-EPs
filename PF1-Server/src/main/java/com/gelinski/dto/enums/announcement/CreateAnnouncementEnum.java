package com.gelinski.dto.enums.announcement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CreateAnnouncementEnum {
    ANNOUNCEMENT_CREATED_SUCCESSFULLY("300", "Successful announcement creation"),
    MISSING_FIELDS("301", "Missing fields"),
    INVALID_TOKEN("302", "Invalid token"),
    UNKNOWN_ERROR("303", "Unknown error");

    private final String code;
    private final String message;
}
