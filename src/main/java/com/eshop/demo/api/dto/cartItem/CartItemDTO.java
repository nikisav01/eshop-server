package com.eshop.demo.api.dto.cartItem;

public record CartItemDTO(Long cartItemID,
                          Long productID,
                          String productName,
                          Integer productPrice,
                          Long cartID,
                          Integer quantity) {
}
