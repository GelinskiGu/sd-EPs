package com.gelinski.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperationEnum {
    LOGIN(5), SIGN_UP(1), LOGOUT(6), READ_ACCOUNT(2), UPDATE_ACCOUNT(3), DELETE_ACCOUNT(4);

    private final Integer op;

    public static OperationEnum getEnumByOp(Integer op) {
        return Arrays.stream(OperationEnum.values())
                .filter(operationEnum -> operationEnum.getOp().equals(op))
                .findFirst()
                .orElse(null);
    }
}
