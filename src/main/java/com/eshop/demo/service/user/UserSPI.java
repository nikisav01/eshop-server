package com.eshop.demo.service.user;

import com.eshop.demo.model.Cart;
import com.eshop.demo.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface UserSPI {

    User create(User user);

    User likeProduct(String username, Long productID);

    Optional<User> readById(Long id);

    Optional<User> readByUsername(String username);

    Collection<User> readAll();

    Cart readCartByUsername(String username);

    User update(String username, User user);

    void savePhoto(String username, MultipartFile multipartFile);

    void deleteById(Long id);

    void deleteByUsername(String username);

}
