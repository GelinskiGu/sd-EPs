package com.gelinski.dto.request.category;

import com.gelinski.dto.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeleteCategoryRequest extends BaseRequestDTO implements Serializable {
    private String token;
    private List<String> categoryIds;
}
