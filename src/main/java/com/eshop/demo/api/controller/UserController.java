package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.user.UserDTO;
import com.eshop.demo.api.dto.user.UserUpdateDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.User;
import com.eshop.demo.service.cart.CartSPI;
import com.eshop.demo.service.user.UserConverter;
import com.eshop.demo.service.user.UserSPI;
import com.eshop.demo.service.user.UserUpdateConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSPI userSPI;

    private final CartSPI cartSPI;
    private final UserConverter userConverter;

    private final UserUpdateConverter userUpdateConverter;

    public UserController(@Qualifier("UserService") UserSPI userSPI,
                          @Qualifier("CartService") CartSPI cartSPI,
                          UserConverter userConverter,
                          UserUpdateConverter userUpdateConverter) {
        this.userSPI = userSPI;
        this.cartSPI = cartSPI;
        this.userConverter = userConverter;
        this.userUpdateConverter = userUpdateConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDTO create(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userConverter.fromModel(userSPI.create(userUpdateConverter.toModel(userUpdateDTO)));
    }

    @PostMapping("/{username}/carts/products/{productID}/{quantity}")
    public UserDTO addProductToCart(@PathVariable("username") String username,
                                    @PathVariable("productID") Long productID,
                                    @PathVariable Integer quantity) {
        User user = userSPI.readByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User " + username + " wasn't found"));
        cartSPI.addProduct(user.getCart(), productID, quantity);
        return userConverter.fromModel(userSPI.readByUsername(username).get());
    }

    @PostMapping("{username}/products/likes/{productID}")
    public UserDTO likeProduct(@PathVariable("username") String username,
                               @PathVariable("productID") Long productID) {
        return userConverter.fromModel(userSPI.likeProduct(username, productID));
    }

    @PostMapping("/{username}/photos")
    public void savePhoto(@PathVariable("username") String username, @RequestParam("photo") MultipartFile multipartFile) {
        userSPI.savePhoto(username, multipartFile);
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userConverter.fromModelMany(userSPI.readAll());
    }

    @GetMapping("/{username}")
    public UserDTO getOneByUsername(@PathVariable String username) {
        return userConverter.fromModel(userSPI.readByUsername(username).orElseThrow(EntityNotFound::new));
    }

    @GetMapping(
            value = "/{id}/photos",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("id") Long id) {
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = new FileInputStream("src/main/resources/serverData/images/userPhotos/" + id + "/photo.jpg");
        } catch (FileNotFoundException e) {
            throw new EntityNotFound();
        }
        try {
            bytes = IOUtils.toByteArray(is);
        } catch (IOException ioe) {
            throw new EntityStateException("Problem with sending response bytes");
        }
        return bytes;
    }

    @PutMapping("/{username}")
    public UserDTO update(@PathVariable String username, @RequestBody UserUpdateDTO userUpdateDTO) {
        return userConverter.fromModel(userSPI.update(username, userUpdateConverter.toModel(userUpdateDTO)));
    }

    @DeleteMapping("/{username}")
    public void deleteOneByUsername(@PathVariable String username) {
        userSPI.deleteByUsername(username);
    }

    @DeleteMapping("/{username}/carts/products/{productID}/{quantity}")
    public UserDTO deleteProductFromCart(@PathVariable("username") String username,
                                    @PathVariable("productID") Long productID,
                                    @PathVariable Integer quantity) {
        User user = userSPI.readByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User " + username + " wasn't found"));
        cartSPI.deleteProduct(user.getCart(), productID, quantity);
        return userConverter.fromModel(userSPI.readByUsername(username).get());
    }

}
