package com.eshop.demo.services.user;

import com.eshop.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserSPI {

    User create(User user);

    Optional<User> readById(Long id);

    Optional<User> readByUsername(String username);

    Collection<User> readAll();

    User update(User user);

    void deleteById(Long id);

    void deleteByUsername(String username);

}
