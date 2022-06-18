package com.eshop.demo.api.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductUpdateDTO {

    private Long productID;

    private String productName;

    private String pathToPhotos;

    private Integer price;

    private Integer quantity;

}
