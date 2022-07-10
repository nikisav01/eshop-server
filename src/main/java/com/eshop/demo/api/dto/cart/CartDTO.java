package com.eshop.demo.api.dto.cart;

import com.eshop.demo.api.dto.cartItem.CartItemDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CartDTO {

    private Long cartID;

    private Long userID;

    private List<CartItemDTO> cartItemsIDs;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastChangeTime;

    private Integer totalPrice;

}
