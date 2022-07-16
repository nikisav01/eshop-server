package com.eshop.demo.api.controller;

import com.eshop.demo.api.dto.product.ProductDTO;
import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.exceptions.UnprocessableRequest;
import com.eshop.demo.model.Product;
import com.eshop.demo.service.image.ImageSPI;
import com.eshop.demo.service.product.ProductConverter;
import com.eshop.demo.service.product.ProductSPI;
import com.eshop.demo.service.product.ProductUpdateConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductSPI productSPI;

    private final ImageSPI<Product> imageSPI;

    private final ProductConverter productConverter;

    private final ProductUpdateConverter productUpdateConverter;

    public ProductController(@Qualifier("ProductService") ProductSPI productSPI,
                             @Qualifier("ProductImageService") ImageSPI<Product> imageSPI,
                             ProductConverter productConverter,
                             ProductUpdateConverter productUpdateConverter) {
        this.productSPI = productSPI;
        this.imageSPI = imageSPI;
        this.productConverter = productConverter;
        this.productUpdateConverter = productUpdateConverter;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductUpdateDTO create(@RequestBody ProductUpdateDTO productDTO) {
        return productUpdateConverter.fromModel(productSPI.create(productUpdateConverter.toModel(productDTO)));
    }

    @PostMapping("/{productID}/photos")
    public void savePhoto(@PathVariable("productID") Long productID,
                          @RequestParam("photo") MultipartFile multipartFile) {
        //productSPI.savePhoto(productID, multipartFile); - save in local storage
        Product product = productSPI.readById(productID)
                .orElseThrow(() -> new EntityNotFound("product " + productID));
        imageSPI.upload(multipartFile, product); // save in database
    }

    @GetMapping(
            value = "/{productID}/photos/{photoNumber}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getPhotoById(@PathVariable("productID") Long productID,
                                             @PathVariable("photoNumber") Integer photoNumber) {
        Product product = productSPI.readById(productID)
                .orElseThrow(() -> new EntityNotFound("product " + productID));
        if (product.getImages().size() - 1 < photoNumber)
            throw new UnprocessableRequest();
        return product.getImages().get(photoNumber).getPhoto();
    }

    @GetMapping
    public Page<ProductUpdateDTO> getAll(@RequestParam int page, @RequestParam int size) {
        return productSPI.readAll(page, size);
    }

    @GetMapping("/{productID}")
    public ProductDTO getOneById(@PathVariable Long productID) {
        return productConverter.fromModel(productSPI.readById(productID)
                .orElseThrow(() -> new EntityNotFound("product " + productID)));
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

    @DeleteMapping("/{productID}/photos/{photoNumber}")
    public void deletePhotoById(@PathVariable("productID") Long productID,
                                @PathVariable("photoNumber") Integer photoNumber) {
        Product product = productSPI.readById(productID)
                .orElseThrow(() -> new EntityNotFound("product " + productID));
        if (product.getImages().size() - 1 < photoNumber)
            throw new UnprocessableRequest();
        imageSPI.deleteById(product.getImages().get(photoNumber).getId());
    }

}
