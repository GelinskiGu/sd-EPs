package com.gelinski.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UpdateAccountRequest extends BaseRequestDTO implements Serializable {
    private String user;
    private String password;
    private String name;
    private String token;
}
