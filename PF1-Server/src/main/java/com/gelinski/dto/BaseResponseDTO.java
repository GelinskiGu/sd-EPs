package com.gelinski.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponseDTO {
    private String code;
    private String message;
}
