package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReadCategoryEnum {
    SUCCESS("210", "Successful category read"),
    MISSING_FIELDS("211", "Missing fields"),
    INVALID_TOKEN("212", "Invalid token"),
    UNKNOWN_ERROR("213", "Unknown error");

    private final String code;
    private final String message;
}
