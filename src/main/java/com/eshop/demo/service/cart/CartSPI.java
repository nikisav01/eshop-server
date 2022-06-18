package com.eshop.demo.service.cart;

import com.eshop.demo.model.Cart;

import java.util.Collection;
import java.util.Optional;

public interface CartSPI {

    Cart create(Cart cart);

    Optional<Cart> readById(Long id);

    Collection<Cart> readAll();

    Cart update(Long id, Cart cart);

    void deleteById(Long id);

    Cart addProduct(Cart cart, Long productID, Integer quantity);

    Cart deleteProduct(Cart cart, Long productID, Integer quantity);

}
