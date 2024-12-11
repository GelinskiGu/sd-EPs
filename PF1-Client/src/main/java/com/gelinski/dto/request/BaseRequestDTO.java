package com.gelinski.dto.request;

import com.gelinski.dto.enums.OperationEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class BaseRequestDTO implements Serializable {
    private Integer op;

    public Boolean validOperation() {
        return Objects.nonNull(OperationEnum.getEnumByOp(op));
    }
}
