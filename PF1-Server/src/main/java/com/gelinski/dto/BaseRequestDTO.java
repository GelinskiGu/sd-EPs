package com.gelinski.dto;

import com.gelinski.dto.enums.OperationEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class BaseRequestDTO {
    private Integer op;

    public Boolean validOperation() {
        return Objects.nonNull(OperationEnum.getEnumByOp(op));
    }
}
