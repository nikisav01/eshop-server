package com.eshop.demo.service.cartItem;

import com.eshop.demo.dao.CartItemJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service("CartItemService")
public class CartItemService implements CartItemSPI {

    private final CartItemJpaRepository repository;

    public CartItemService(CartItemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        repository.save(cartItem);
        return repository.findById(cartItem.getCartItemID()).get();
    }

    @Override
    public Collection<CartItem> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<CartItem> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CartItem update(Long id, CartItem cartItem) {
        if (!id.equals(cartItem.getCartItemID()))
            throw new IncorrectRequest("id " + id + " doesn't match cartItems id " + cartItem.getCartItemID());
        repository.save(cartItem);
        return repository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }
}
