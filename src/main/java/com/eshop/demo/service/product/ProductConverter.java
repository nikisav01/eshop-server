package com.eshop.demo.service.product;

import com.eshop.demo.api.dto.product.ProductDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.Category;
import com.eshop.demo.model.Product;
import com.eshop.demo.model.User;
import com.eshop.demo.service.cart.CartSPI;
import com.eshop.demo.service.category.CategorySPI;
import com.eshop.demo.service.user.UserSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConverter {

    private final CategorySPI categorySPI;

    private final CartSPI cartSPI;

    private final UserSPI userSPI;

    public ProductConverter(@Qualifier("CategoryService") CategorySPI categorySPI,
                            @Qualifier("CartService") CartSPI cartSPI,
                            @Qualifier("UserService") UserSPI userSPI) {
        this.categorySPI = categorySPI;
        this.cartSPI = cartSPI;
        this.userSPI = userSPI;
    }

    public Product toModel(ProductDTO productDTO) {
        return new Product(
                productDTO.getProductID(),
                productDTO.getProductName(),
                productDTO.getPathToPhotos(),
                productDTO.getCategoriesIDs().stream()
                        .map(categoryID -> categorySPI.readById(categoryID).orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList()),
                productDTO.getPrice(),
                productDTO.getQuantity(),
                new ArrayList<>(),
                new ArrayList<>(),
                productDTO.getUsersSavedIDs().stream()
                        .map(userID -> userSPI.readById(userID).orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList())
        );
    }

    public ProductDTO fromModel(Product product) {
        return new ProductDTO(
                product.getProductID(),
                product.getProductName(),
                product.getPathToPhotos(),
                product.getCategories().stream()
                        .map(Category::getCategoryID)
                        .collect(Collectors.toList()),
                product.getPrice(),
                product.getQuantity(),
                product.getUsersSaved().stream()
                        .map(User::getUserID)
                        .collect(Collectors.toList())
        );
    }

    public List<Product> toModelMany(Collection<ProductDTO> productDTOS) {
        return productDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> fromModelMany(Collection<Product> products) {
        return products.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
