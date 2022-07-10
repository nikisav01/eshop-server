package com.eshop.demo.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDTO {

    private Long productID;

    private String productName;

    private String pathToPhotos;

    private List<Long> categoriesIDs;

    private Integer price;

    private Integer quantity;

    private List<Long> usersSavedIDs;

}
