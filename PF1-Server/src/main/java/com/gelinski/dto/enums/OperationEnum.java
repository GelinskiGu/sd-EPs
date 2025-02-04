package com.gelinski.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperationEnum {
    LOGIN("5"), SIGN_UP("1"), LOGOUT("6");

    private final String op;

    public static OperationEnum getEnumByOp(String op) {
        return Arrays.stream(OperationEnum.values())
                .filter(operationEnum -> operationEnum.getOp().equals(op))
                .findFirst()
                .orElse(null);
    }
}
