package com.eshop.demo.service.user;

import com.eshop.demo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserSPI {

    User create(User user);

    Optional<User> readById(Long id);

    Optional<User> readByUsername(String username);

    Collection<User> readAll();

    User update(String username, User user);

    void deleteById(Long id);

    void deleteByUsername(String username);

}
