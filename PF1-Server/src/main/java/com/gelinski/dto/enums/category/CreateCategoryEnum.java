package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CreateCategoryEnum {
    CATEGORY_CREATED_SUCCESSFULLY("200", "Category created successfully"),
    MISSING_FIELDS("201", "Missing fields"),
    INVALID_TOKEN("202", "Invalid token"),
    UNKNOWN_ERROR("203", "Unknown error");

    private final String code;
    private final String message;
}
