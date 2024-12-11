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
public class LogoutRequest extends BaseRequestDTO implements Serializable {
    private String token;

    public Boolean fieldsMissing() {
        return Stream.of(token).anyMatch(field -> Objects.isNull(field) || field.isBlank());
    }
}
