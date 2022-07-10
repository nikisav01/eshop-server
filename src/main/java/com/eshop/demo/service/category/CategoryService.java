package com.eshop.demo.service.category;

import com.eshop.demo.dao.CategoryJpaRepository;
import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.model.Category;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("CategoryService")
public class CategoryService implements CategorySPI {

    private final CategoryJpaRepository repository;

    private final ProductJpaRepository productRepository;

    public CategoryService(CategoryJpaRepository repository, ProductJpaRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    @Override
    public Category create(Category category) {
        return repository.save(category);
    }

    @Override
    public Optional<Category> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Category> readAll() {
        return repository.findAll();
    }

    @Override
    public Category update(Long id, Category category) {
        if (repository.findById(category.getCategoryID()).isEmpty())
            throw new EntityNotFound(category);
        repository.save(category);
        return repository.findById(category.getCategoryID()).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }

    @Override
    public Category addProduct(Long categoryID, Long productID) {
        Category category = repository.findById(categoryID).orElseThrow(EntityNotFound::new);
        Product product = productRepository.findById(productID).orElseThrow(EntityNotFound::new);
        List<Product> products = category.getProducts();
        if (!products.contains(product))
            products.add(product);
        repository.save(category);
        return repository.findById(categoryID).get();
    }

}
