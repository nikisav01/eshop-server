package com.eshop.demo.service.user;

import com.eshop.demo.dao.CartJpaRepository;
import com.eshop.demo.dao.UserJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.Cart;
import com.eshop.demo.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service("UserService")
public class UserService implements UserSPI {

    private final UserJpaRepository repository;
    private final CartJpaRepository cartRepository;

    public UserService(UserJpaRepository repository,
                       CartJpaRepository cartRepository) {
        this.repository = repository;
        this.cartRepository = cartRepository;
    }

    @Override
    public User create(User user) {
        if (repository.findByUsername(user.getUsername()).isPresent())
            throw new EntityStateException(user);
        repository.save(user);
        Cart cart = new Cart(user.getUserID(), user, new ArrayList<>(), LocalDateTime.now(), 0);
        cartRepository.save(cart);
        user.setCart(cart);
        return repository.findById(user.getUserID()).get();
    }

    @Override
    public Optional<User> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> readByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Collection<User> readAll() {
        return repository.findAll();
    }

    @Override
    public User update(String username, User user) {
        if (repository.findByUsername(user.getUsername()).isEmpty())
            throw new EntityNotFound(user);
        User oldUser = repository.findByUsername(user.getUsername()).get();
        oldUser.setUsername(user.getUsername());
        oldUser.setSurname(user.getSurname());
        oldUser.setName(user.getName());
        oldUser.setAddress(user.getAddress());
        repository.save(oldUser);
        return repository.findById(oldUser.getUserID()).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.delete(
                repository.findByUsername(username).orElseThrow(EntityNotFound::new)
        );
    }

}
