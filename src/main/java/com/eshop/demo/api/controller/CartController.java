package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.cart.CartDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.service.cart.CartConverter;
import com.eshop.demo.service.cart.CartSPI;
import com.eshop.demo.service.user.UserSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartSPI cartSPI;

    private final UserSPI userSPI;

    private final CartConverter cartConverter;

    public CartController(@Qualifier("CartService") CartSPI cartSPI,
                          UserSPI userSPI,
                          CartConverter cartConverter) {
        this.cartSPI = cartSPI;
        this.userSPI = userSPI;
        this.cartConverter = cartConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartDTO create(@RequestBody CartDTO cartDTO) {
        return cartConverter.fromModel(cartSPI.create(cartConverter.toModel(cartDTO)));
    }

    @GetMapping
    public List<CartDTO> getAll() {
        return cartConverter.fromModelMany(cartSPI.readAll());
    }

    @GetMapping("/{id}")
    public CartDTO getOneById(@PathVariable Long id) {
        return cartConverter.fromModel(cartSPI.readById(id).orElseThrow(EntityNotFound::new));
    }

    @GetMapping("/users/{username}")
    public CartDTO getOneByUsername(@PathVariable String username) {
        return cartConverter.fromModel(userSPI.readCartByUsername(username));
    }

    @PutMapping("/{id}")
    public CartDTO update(@PathVariable Long id, @RequestBody CartDTO cartDTO) {
        return cartConverter.fromModel(
                cartSPI.update(id, cartConverter.toModel(cartDTO))
        );
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        cartSPI.deleteById(id);
    }

}
