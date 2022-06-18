package com.eshop.demo.service.cartItem;

import com.eshop.demo.model.CartItem;

import java.util.Collection;
import java.util.Optional;

public interface CartItemSPI {

    CartItem create(CartItem cartItem);

    Collection<CartItem> readAll();

    Optional<CartItem> readById(Long id);

    CartItem update(Long id, CartItem cartItem);

    void deleteById(Long id);

}
