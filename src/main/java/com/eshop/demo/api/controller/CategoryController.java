package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.Views;
import com.eshop.demo.api.dto.category.CategoryDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.service.category.CategoryConverter;
import com.eshop.demo.service.category.CategorySPI;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategorySPI categorySPI;

    private final CategoryConverter categoryConverter;

    public CategoryController(@Qualifier("CategoryService") CategorySPI categorySPI,
                              CategoryConverter categoryConverter) {
        this.categorySPI = categorySPI;
        this.categoryConverter = categoryConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO categoryDTO) {
        return categoryConverter.fromModel(categorySPI.create(categoryConverter.toModel(categoryDTO)));
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryConverter.fromModelMany(categorySPI.readAll());
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/{id}")
    public CategoryDTO getOneById(@PathVariable Long id) {
        return categoryConverter.fromModel(categorySPI.readById(id).orElseThrow(EntityNotFound::new));
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryConverter.fromModel(
                categorySPI.update(id, categoryConverter.toModel(categoryDTO))
        );
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        categorySPI.deleteById(id);
    }

    @PostMapping("/{categoryID}/products/{productID}")
    public CategoryDTO addProductToCategory(@PathVariable("categoryID") Long categoryID,
                                            @PathVariable("productID") Long productID) {
        return categoryConverter.fromModel(categorySPI.addProduct(categoryID, productID));
    }

}
