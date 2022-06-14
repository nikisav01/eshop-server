package com.eshop.demo.services.product;

import com.eshop.demo.model.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductSPI {

    Product create(Product product);

    Optional<Product> readById(Long id);

    Collection<Product> readAll();

    Product update(Product product);

    void deleteById(Long id);

}
