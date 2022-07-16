package com.eshop.demo.service.image;

import com.eshop.demo.dao.ImageJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.IncorrectRequest;
import com.eshop.demo.model.Image;
import com.eshop.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("ProductImageService")
public class ProductImageService implements ImageSPI<Product> {

    private final ImageJpaRepository repository;

    public ProductImageService(ImageJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void upload(MultipartFile multipartFile, Product product) {
        try {
            Image image = new Image(null, multipartFile.getBytes(), multipartFile.getName(), product);
            repository.save(image);
        } catch (IOException e) {
            throw new IncorrectRequest();
        }
    }

    @Override
    public byte[] getById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFound::new).getPhoto();
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFound();
        repository.deleteById(id);
    }
}
