package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.product.ProductDTO;
import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.service.product.ProductConverter;
import com.eshop.demo.service.product.ProductSPI;
import com.eshop.demo.service.product.ProductUpdateConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductSPI productSPI;

    private final ProductConverter productConverter;

    private final ProductUpdateConverter productUpdateConverter;

    public ProductController(@Qualifier("ProductService") ProductSPI productSPI,
                             ProductConverter productConverter,
                             ProductUpdateConverter productUpdateConverter) {
        this.productSPI = productSPI;
        this.productConverter = productConverter;
        this.productUpdateConverter = productUpdateConverter;
    }

    @PostMapping
    public ProductUpdateDTO create(@RequestBody ProductUpdateDTO productDTO) {
        return productUpdateConverter.fromModel(productSPI.create(productUpdateConverter.toModel(productDTO)));
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productConverter.fromModelMany(productSPI.readAll());
    }

    @GetMapping("/{id}")
    public ProductDTO getOneById(@PathVariable Long id) {
        return productConverter.fromModel(productSPI.readById(id).orElseThrow(EntityNotFound::new));
    }

    @GetMapping("/categories/{categoryID}")
    public List<ProductUpdateDTO> getByCategoryId(@PathVariable Long categoryID) {
        return productUpdateConverter.fromModelMany(productSPI.readByCategoryId(categoryID));
    }

    @PutMapping("/{id}")
    public ProductUpdateDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        return productUpdateConverter.fromModel(
                productSPI.update(id, productUpdateConverter.toModel(productUpdateDTO))
        );
    }

    @DeleteMapping("/{id}")
    public void deleteOneById(@PathVariable Long id) {
        productSPI.deleteById(id);
    }

}
