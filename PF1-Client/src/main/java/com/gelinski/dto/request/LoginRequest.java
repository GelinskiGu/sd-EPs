package com.gelinski.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LoginRequest extends BaseRequestDTO implements Serializable {
    private String user;
    private String password;

    public Boolean fieldsMissing() {
        return Stream.of(user, password)
                .anyMatch(field -> Objects.isNull(field) || field.isBlank());
    }
}
