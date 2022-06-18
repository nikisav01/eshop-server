package com.eshop.demo.service.product;

import com.eshop.demo.model.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductSPI {

    Product create(Product product);

    Optional<Product> readById(Long id);

    Collection<Product> readByCategoryId(Long categoryID);

    Collection<Product> readAll();

    Product update(Long id, Product product);

    void deleteById(Long id);

}
