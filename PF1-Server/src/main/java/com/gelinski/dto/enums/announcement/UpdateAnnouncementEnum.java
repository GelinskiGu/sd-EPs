package com.gelinski.dto.enums.announcement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateAnnouncementEnum {
    ANNOUNCEMENT_UPDATED_SUCCESSFULLY("320", "Successful announcement update"),
    MISSING_FIELDS("321", "Missing fields"),
    INVALID_TOKEN("322", "Invalid token"),
    BAD_REQUEST("323", "Invalid information inserted"),
    UNKNOWN_ERROR("324", "Unknown error");

    private final String code;
    private final String message;
}
