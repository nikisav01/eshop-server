package com.eshop.demo.services.category;

import com.eshop.demo.dao.CategoryJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryService implements CategorySPI {

    private final CategoryJpaRepository repository;

    public CategoryService(CategoryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category category) {
        if (repository.findById(category.getCategoryID()).isPresent())
            throw new EntityStateException(category);
        repository.save(category);
        return repository.findById(category.getCategoryID()).get();
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
    public Category update(Category category) {
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
}
