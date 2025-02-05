package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteCategoryEnum {
    SUCCESS("230", "Successful category deletion"),
    MISSING_FIELDS("231", "Missing fields"),
    INVALID_TOKEN("232", "Invalid token"),
    INVALID_INPUT("233", "Invalid information inserted"),
    UNKNOWN_ERROR("234", "Unknown error");

    private final String code;
    private final String message;
}
