package com.gelinski.dto.response.account;

import com.gelinski.dto.BaseResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LoginResponse extends BaseResponseDTO implements Serializable {
    private String token;
}
