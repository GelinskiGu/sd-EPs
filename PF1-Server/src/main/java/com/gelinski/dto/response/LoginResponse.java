package com.gelinski.dto.response;

import com.gelinski.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LoginResponse extends BaseResponseDTO {
    private String token;
}
