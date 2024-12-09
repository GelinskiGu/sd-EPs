package com.gelinski.dto.request;

import com.gelinski.dto.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CreateAccountRequest extends BaseRequestDTO {
    private String user;
    private String password;
    private String name;

    public Boolean fieldsMissing() {
        return Stream.of(user, password, name)
                .anyMatch(field -> Objects.isNull(field) || field.isBlank());
    }
}
