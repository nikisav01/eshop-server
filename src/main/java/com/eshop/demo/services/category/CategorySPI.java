package com.eshop.demo.services.category;

import com.eshop.demo.model.Category;

import java.util.Collection;
import java.util.Optional;

public interface CategorySPI {

    Category create(Category category);

    Optional<Category> readById(Long id);

    Collection<Category> readAll();

    Category update(Category category);

    void deleteById(Long id);

}
