package com.gelinski.dto.enums.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscribeToCategoryEnum {
    SUBSCRIBE_SUCCESSFUL("340", "Successful subscription"),
    MISSING_FIELDS("341", "Missing fields"),
    INVALID_TOKEN("342", "Invalid token"),
    BAD_REQUEST("343", "Invalid information inserted"),
    UNKNOWN_ERROR("344", "Unknown error");

    private final String code;
    private final String message;
}
