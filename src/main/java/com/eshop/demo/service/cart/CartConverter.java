package com.eshop.demo.service.cart;

import com.eshop.demo.api.dto.cart.CartDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.Cart;
import com.eshop.demo.service.cartItem.CartItemConverter;
import com.eshop.demo.service.cartItem.CartItemSPI;
import com.eshop.demo.service.user.UserSPI;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartConverter {

    private final UserSPI userSPI;

    private final CartItemSPI cartItemSPI;

    private final CartItemConverter cartItemConverter;

    public CartConverter(UserSPI userSPI,
                         CartItemSPI cartItemSPI,
                         CartItemConverter cartItemConverter) {
        this.userSPI = userSPI;
        this.cartItemSPI = cartItemSPI;
        this.cartItemConverter = cartItemConverter;
    }

    public Cart toModel(CartDTO cartDTO) {
        return new Cart(
                cartDTO.getCartID(),
                userSPI.readById(cartDTO.getUserID()).orElseThrow(EntityNotFound::new),
                cartDTO.getCartItems().stream()
                        .map(cartItem -> cartItemSPI.readById(cartItem.cartID()).orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList()),
                cartDTO.getLastChangeTime(),
                cartDTO.getTotalPrice()
        );
    }

    public CartDTO fromModel(Cart cart) {
        return new CartDTO(
                cart.getCartID(),
                cart.getUser().getUserID(),
                cart.getCartItems().stream()
                        .map(cartItemConverter::fromModel)
                        .collect(Collectors.toList()),
                cart.getLastChangeTime(),
                cart.getTotalPrice()
        );
    }

    public List<Cart> toModelMany(Collection<CartDTO> cartDTOS) {
        return cartDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<CartDTO> fromModelMany(Collection<Cart> carts) {
        return carts.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
