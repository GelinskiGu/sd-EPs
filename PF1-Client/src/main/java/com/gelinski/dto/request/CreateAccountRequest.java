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
public class CreateAccountRequest extends BaseRequestDTO implements Serializable {
    private String user;
    private String password;
    private String name;

    public Boolean fieldsMissing() {
        return Stream.of(user, password, name)
                .anyMatch(field -> Objects.isNull(field) || field.isBlank());
    }
}
