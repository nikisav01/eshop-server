package com.eshop.demo.service.cart;

import com.eshop.demo.dao.CartItemJpaRepository;
import com.eshop.demo.dao.CartJpaRepository;
import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.exceptions.UnprocessableRequest;
import com.eshop.demo.model.Cart;
import com.eshop.demo.model.CartItem;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service("CartService")
public class CartService implements CartSPI {

    private final CartJpaRepository repository;

    private final ProductJpaRepository productRepository;

    private final CartItemJpaRepository cartItemRepository;

    public CartService(CartJpaRepository repository,
                       ProductJpaRepository productRepository,
                       CartItemJpaRepository cartItemRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart create(Cart cart) {
        if (repository.findById(cart.getCartID()).isPresent())
            throw new EntityStateException(cart);
        cart.setTotalPrice(0);
        return repository.save(cart);
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
    public Cart update(Long id, Cart cart) {
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

    @Override
    public Cart addProduct(Cart cart, Long productID, Integer quantity) {
        if (!repository.existsById(cart.getCartID()))
            throw new IncorrectRequest();
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFound("Product + " + productID + " wasn't found."));
        if (quantity > product.getQuantity())
            throw new UnprocessableRequest("Not enough products in storage.");
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        if (cart.getCartItems().stream().noneMatch(cartItem -> {
            if (cartItem.getProduct().getProductID().equals(productID)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                return true;
            };
            return false;
        })) {
            CartItem item = new CartItem(null, product, cart, quantity);
            cartItemRepository.save(item);
            cart.getCartItems().add(item);
        }
        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantity);
        cart.setLastChangeTime(LocalDateTime.now());
        repository.save(cart);
        return repository.findById(cart.getCartID()).get();
    }

    @Override
    public Cart deleteProduct(Cart cart, Long productID, Integer quantity) {
        if (!repository.existsById(cart.getCartID()))
            throw new IncorrectRequest();
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFound("Product + " + productID + " wasn't found."));
        CartItem cartItem = cart.getCartItems().stream()
                .filter(cartIt -> cartIt.getProduct().equals(product))
                .findFirst().orElseThrow(
                        () -> new EntityNotFound("Product " + productID + " isn't present in cart")
                );
        if (cartItem.getQuantity() <= quantity) {
            cart.setTotalPrice(cart.getTotalPrice() - product.getPrice() * cartItem.getQuantity());
            product.setQuantity(product.getQuantity() + cartItem.getQuantity());
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }
        else {
            product.setQuantity(product.getQuantity() + quantity);
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cart.setTotalPrice(cart.getTotalPrice() - product.getPrice() * quantity);
            cartItemRepository.save(cartItem);
        }
        cart.setLastChangeTime(LocalDateTime.now());
        productRepository.save(product);
        repository.save(cart);
        return repository.findById(cart.getCartID()).get();
    }
}
