package com.eshop.demo.service.cartItem;

import com.eshop.demo.api.dto.cartItem.CartItemDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.CartItem;
import com.eshop.demo.service.cart.CartSPI;
import com.eshop.demo.service.product.ProductSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemConverter {

    private final CartSPI cartSPI;

    private final ProductSPI productSPI;

    public CartItemConverter(@Qualifier("CartService") CartSPI cartSPI,
                             @Qualifier("ProductService") ProductSPI productSPI) {
        this.cartSPI = cartSPI;
        this.productSPI = productSPI;
    }

    public CartItem toModel(CartItemDTO cartItemDTO) {
        return new CartItem(
                cartItemDTO.cartItemID(),
                productSPI.readById(cartItemDTO.productID()).orElseThrow(EntityNotFound::new),
                cartSPI.readById(cartItemDTO.cartID()).orElseThrow(EntityNotFound::new),
                cartItemDTO.quantity()
        );
    }

    public CartItemDTO fromModel(CartItem cartItem) {
        return new CartItemDTO(
                cartItem.getCartItemID(),
                cartItem.getProduct().getProductID(),
                cartItem.getCart().getCartID(),
                cartItem.getQuantity()
        );
    }

    public List<CartItem> toModelMany(Collection<CartItemDTO> cartItemDTOS) {
        return cartItemDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<CartItemDTO> fromModelMany(Collection<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
