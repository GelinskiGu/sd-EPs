package com.gelinski.dto.request.category;

import com.gelinski.dto.request.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CreateCategoryRequest extends BaseRequestDTO implements Serializable {
    private String token;
    private List<Category> categories;
}
