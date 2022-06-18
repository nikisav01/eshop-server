package com.eshop.demo.service.product;

import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductUpdateConverter {

    public Product toModel(ProductUpdateDTO productUpdateDTO) {
        return new Product(
                productUpdateDTO.getProductID(),
                productUpdateDTO.getProductName(),
                productUpdateDTO.getPathToPhotos(),
                new ArrayList<>(),
                productUpdateDTO.getPrice(),
                productUpdateDTO.getQuantity(),
                null,
                new ArrayList<>()
        );
    }

    public ProductUpdateDTO fromModel(Product product) {
        return new ProductUpdateDTO(
                product.getProductID(),
                product.getProductName(),
                product.getPathToPhotos(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    public List<Product> toModelMany(Collection<ProductUpdateDTO> productUpdateDTOS) {
        return productUpdateDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<ProductUpdateDTO> fromModelMany(Collection<Product> products) {
        return products.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
