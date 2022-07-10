package com.eshop.demo.api.dto.category;

import com.eshop.demo.api.dto.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryDTO {

    @JsonView(Views.Public.class)
    private Long categoryID;

    @JsonView(Views.Public.class)
    private String categoryName;

    @JsonView(Views.Internal.class)
    private List<Long> productsIDs;

}
