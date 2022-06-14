package com.eshop.demo.services.product;

import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProductService implements ProductSPI {

    private final ProductJpaRepository repository;

    public ProductService(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {
        if (repository.findById(product.getProductID()).isPresent())
            throw new EntityStateException(product);
        repository.save(product);
        return repository.findById(product.getProductID()).get();
    }

    @Override
    public Optional<Product> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Product> readAll() {
        return repository.findAll();
    }

    @Override
    public Product update(Product product) {
        if (repository.findById(product.getProductID()).isEmpty())
            throw new EntityNotFound(product);
        repository.save(product);
        return repository.findById(product.getProductID()).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }
}
