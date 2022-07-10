package com.eshop.demo.service.category;

import com.eshop.demo.api.dto.category.CategoryDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.Category;
import com.eshop.demo.model.Product;
import com.eshop.demo.service.product.ProductSPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {

    private final ProductSPI productSPI;

    public CategoryConverter(@Qualifier("ProductService") ProductSPI productSPI) {
        this.productSPI = productSPI;
    }

    public Category toModel(CategoryDTO categoryDTO) {
        return new Category(
                categoryDTO.getCategoryID(),
                categoryDTO.getCategoryName(),
                categoryDTO.getProductsIDs().stream()
                        .map(productID -> productSPI.readById(productID).orElseThrow(EntityNotFound::new))
                        .collect(Collectors.toList())
        );
    }

    public CategoryDTO fromModel(Category category) {
        return new CategoryDTO(
                category.getCategoryID(),
                category.getCategoryName(),
                category.getProducts().stream()
                        .map(Product::getProductID)
                        .collect(Collectors.toList())
        );
    }

    public List<Category> toModelMany(Collection<CategoryDTO> categoryDTOS) {
        return categoryDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> fromModelMany(Collection<Category> categories) {
        return categories.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
