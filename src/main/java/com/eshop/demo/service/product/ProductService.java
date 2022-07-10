package com.eshop.demo.service.product;

import com.eshop.demo.api.dto.product.ProductUpdateDTO;
import com.eshop.demo.dao.CategoryJpaRepository;
import com.eshop.demo.dao.ProductJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.model.Category;
import com.eshop.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static com.eshop.demo.service.FileManagement.saveFile;

@Service("ProductService")
public class ProductService implements ProductSPI {

    private final ProductUpdateConverter converter;

    private final ProductJpaRepository repository;

    private final CategoryJpaRepository categoryRepository;

    public ProductService(ProductUpdateConverter converter,
                          ProductJpaRepository repository,
                          CategoryJpaRepository categoryRepository) {
        this.converter = converter;
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }

    @Override
    public Optional<Product> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<ProductUpdateDTO> readByCategoryId(Long categoryID, int page, int size) {
        if (size <= 0 || page < 0)
            throw new IncorrectRequest();
        Category category = categoryRepository.findById(categoryID).orElseThrow(EntityNotFound::new);
        return repository.findProductsByCategories(category, PageRequest.of(page, size)).map(converter::fromModel);
    }

    @Override
    public Page<ProductUpdateDTO> readAll(int page, int size) {
        if (size <= 0 || page < 0)
            throw new IncorrectRequest();
        return repository.findAll(PageRequest.of(page, size)).map(converter::fromModel);
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
    public void savePhoto(Long id, MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new IncorrectRequest();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Product product = repository.findById(id).orElseThrow(EntityNotFound::new);
        String uploadDir = "src/main/resources/serverData/images/productsPhotos/" + product.getProductID();
        product.setPathToPhotos(uploadDir);
        saveFile(uploadDir, "photo." + fileName.split("\\.")[1], multipartFile);
        repository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }
}
