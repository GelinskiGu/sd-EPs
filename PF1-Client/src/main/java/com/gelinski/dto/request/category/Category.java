package com.gelinski.dto.request.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Category implements Serializable {
    private String id;
    private String name;
    private String description;
}
