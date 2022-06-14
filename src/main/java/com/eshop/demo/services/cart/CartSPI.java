package com.eshop.demo.services.cart;

import com.eshop.demo.model.Cart;

import java.util.Collection;
import java.util.Optional;

public interface CartSPI {

    Cart create(Cart cart);

    Optional<Cart> readById(Long id);

    Collection<Cart> readAll();

    Cart update(Cart cart);

    void deleteById(Long id);

}
