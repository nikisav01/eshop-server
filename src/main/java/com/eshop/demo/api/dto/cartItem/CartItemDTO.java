package com.eshop.demo.api.dto.cartItem;

public record CartItemDTO(Long cartItemID,
                          Long productID,
                          Long cartID,
                          Integer quantity) {
}
