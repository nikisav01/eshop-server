package com.eshop.demo.service.category;

import com.eshop.demo.model.Category;

import java.util.Collection;
import java.util.Optional;

public interface CategorySPI {

    Category create(Category category);

    Optional<Category> readById(Long id);

    Collection<Category> readAll();

    Category update(Long id, Category category);

    void deleteById(Long id);

    Category addProduct(Long categoryID, Long productID);

}
