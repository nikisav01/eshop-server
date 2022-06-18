package com.eshop.demo.service.user;

import com.eshop.demo.api.dto.user.UserDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.Product;
import com.eshop.demo.model.Role;
import com.eshop.demo.model.User;
import com.eshop.demo.service.cart.CartSPI;
import com.eshop.demo.service.product.ProductSPI;
import com.eshop.demo.service.role.RoleSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserConverter {

    private final RoleSPI roleSPI;
    private final CartSPI cartSPI;
    private final ProductSPI productSPI;

    public User toModel(UserDTO userDTO) {
        return new User(
                userDTO.getUserID(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getSurname(),
                userDTO.getName(),
                userDTO.getAddress(),
                userDTO.getRolesIDs().stream()
                        .map(roleID -> roleSPI.readById(roleID)
                                .orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList()),
                cartSPI.readById(userDTO.getCartID()).orElseThrow(EntityNotFound::new),
                userDTO.getLikedProductsIDs().stream()
                        .map(productID -> productSPI.readById(productID)
                                .orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList())
        );
    }

    public UserDTO fromModel(User user) {
        return new UserDTO(
                user.getUserID(),
                user.getUsername(),
                user.getPassword(),
                user.getSurname(),
                user.getName(),
                user.getAddress(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList()),
                user.getCart().getCartID(),
                user.getLikedProducts().stream()
                        .map(Product::getProductID)
                        .collect(Collectors.toList())
        );
    }

    public List<User> toModelMany(Collection<UserDTO> userDTOS) {
        return userDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<UserDTO> fromModelMany(Collection<User> users) {
        return users.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
