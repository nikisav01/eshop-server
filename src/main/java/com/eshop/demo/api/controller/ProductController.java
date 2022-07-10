package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.product.ProductDTO;
import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.service.product.ProductConverter;
import com.eshop.demo.service.product.ProductSPI;
import com.eshop.demo.service.product.ProductUpdateConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductUpdateDTO create(@RequestBody ProductUpdateDTO productDTO) {
        return productUpdateConverter.fromModel(productSPI.create(productUpdateConverter.toModel(productDTO)));
    }

    @PostMapping("{productID}/photos")
    public void savePhoto(@PathVariable("productID") Long productID, @RequestParam("photo") MultipartFile multipartFile) {
        productSPI.savePhoto(productID, multipartFile);
    }

    @GetMapping
    public Page<ProductUpdateDTO> getAll(@RequestParam int page, @RequestParam int size) {
        return productSPI.readAll(page, size);
    }

    @GetMapping("/{id}")
    public ProductDTO getOneById(@PathVariable Long id) {
        return productConverter.fromModel(productSPI.readById(id).orElseThrow(EntityNotFound::new));
    }

    @GetMapping("/categories/{categoryID}")
    public Page<ProductUpdateDTO> getByCategoryId(@PathVariable Long categoryID,
                                                  @RequestParam int page,
                                                  @RequestParam int size) {
        return productSPI.readByCategoryId(categoryID, page, size);
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
