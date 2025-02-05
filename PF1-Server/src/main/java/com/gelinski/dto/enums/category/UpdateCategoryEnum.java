package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateCategoryEnum {
    SUCCESS("220", "Successful category update"),
    MISSING_FIELDS("221", "Missing fields"),
    INVALID_TOKEN("222", "Invalid token"),
    INVALID_INPUT("223", "Invalid information inserted"),
    UNKNOWN_ERROR("224", "Unknown error");

    private final String code;
    private final String message;
}
