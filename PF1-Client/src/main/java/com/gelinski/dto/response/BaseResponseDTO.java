package com.gelinski.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BaseResponseDTO implements Serializable {
    private String response;
    private String message;
}
