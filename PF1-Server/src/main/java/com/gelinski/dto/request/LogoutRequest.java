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
public class LogoutRequest extends BaseRequestDTO {
    private String token;

    public Boolean fieldsMissing() {
        return Stream.of(token).anyMatch(field -> Objects.isNull(field) || field.isBlank());
    }
}
