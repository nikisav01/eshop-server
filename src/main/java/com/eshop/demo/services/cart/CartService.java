package com.eshop.demo.services.cart;

import com.eshop.demo.dao.CartJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.Cart;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CartService implements CartSPI {

    private final CartJpaRepository repository;

    public CartService(CartJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart create(Cart cart) {
        if (repository.findById(cart.getCartID()).isPresent())
            throw new EntityStateException(cart);
        repository.save(cart);
        return repository.findById(cart.getCartID()).get();
    }

    @Override
    public Optional<Cart> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Cart> readAll() {
        return repository.findAll();
    }

    @Override
    public Cart update(Cart cart) {
        if (repository.findById(cart.getCartID()).isEmpty())
            throw new EntityNotFound(cart);
        repository.save(cart);
        return repository.findById(cart.getCartID()).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }
}
