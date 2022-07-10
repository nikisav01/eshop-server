package com.eshop.demo.service.user;

import com.eshop.demo.dao.CartJpaRepository;
import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.dao.UserJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.model.Cart;
import com.eshop.demo.model.Product;
import com.eshop.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.eshop.demo.service.FileManagement.saveFile;

@Service("UserService")
public class UserService implements UserSPI {

    private final UserJpaRepository repository;
    private final CartJpaRepository cartRepository;

    private final ProductJpaRepository productRepository;

    public UserService(UserJpaRepository repository,
                       CartJpaRepository cartRepository,
                       ProductJpaRepository productRepository) {
        this.repository = repository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
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
    public User likeProduct(String username, Long productID) {
        User user = repository.findByUsername(username)
                .orElseThrow(EntityNotFound::new);
        Product product = productRepository.findById(productID)
                .orElseThrow(EntityNotFound::new);
        List<Product> products = user.getLikedProducts();
        if (!products.contains(product))
            products.add(product);
        repository.save(user);
        return repository.findByUsername(username).get();
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
    public void savePhoto(String username, MultipartFile multipartFile) {
        if (multipartFile.isEmpty() || username.isEmpty())
            throw new IncorrectRequest();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        User user = repository.findByUsername(username).orElseThrow(EntityNotFound::new);
        String uploadDir = "src/main/resources/serverData/images/usersPhotos/" + user.getUserID();
        user.setPathToPhoto(uploadDir);
        saveFile(uploadDir, "photo." + fileName.split("\\.")[1], multipartFile);
        repository.save(user);
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
