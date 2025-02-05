package com.gelinski.dto.request.category;


import com.gelinski.dto.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReadCategoryRequest extends BaseRequestDTO implements Serializable {
    private String token;
}
