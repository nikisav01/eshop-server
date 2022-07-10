package com.eshop.demo.service.product;

import com.eshop.demo.api.dto.product.ProductDTO;
import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Optional;

public interface ProductSPI {

    Product create(Product product);

    Optional<Product> readById(Long id);

    Page<ProductUpdateDTO> readByCategoryId(Long categoryID, int page, int size);

    Page<ProductUpdateDTO> readAll(int page, int size);

    Product update(Long id, Product product);

    void savePhoto(Long id, MultipartFile multipartFile);

    void deleteById(Long id);

}
