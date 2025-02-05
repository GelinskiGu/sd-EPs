package com.gelinski.dto.response.category;

import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.request.category.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReadCategoryResponse extends BaseResponseDTO implements Serializable {
    private List<Category> categories;
}
