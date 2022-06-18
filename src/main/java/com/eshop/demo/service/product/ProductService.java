package com.eshop.demo.service.product;

import com.eshop.demo.dao.CategoryJpaRepository;
import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service("ProductService")
public class ProductService implements ProductSPI {

    private final ProductJpaRepository repository;

    private final CategoryJpaRepository categoryRepository;

    public ProductService(ProductJpaRepository repository,
                          CategoryJpaRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product create(Product product) {
        repository.save(product);
        return repository.findById(product.getProductID()).get();
    }

    @Override
    public Optional<Product> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Product> readByCategoryId(Long categoryID) {
        return categoryRepository.findById(categoryID).orElseThrow(
                () -> new EntityNotFound("category " + categoryID)
        ).getProducts();
    }

    @Override
    public Collection<Product> readAll() {
        return repository.findAll();
    }

    @Override
    public Product update(Long id, Product product) {
        if (!id.equals(product.getProductID()))
            throw new IncorrectRequest("id doesn't match productID.");
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
