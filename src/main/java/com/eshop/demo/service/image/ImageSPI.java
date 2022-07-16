package com.eshop.demo.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageSPI<E> {

    void upload(MultipartFile multipartFile, E entity);

    byte[] getById(Long id);

    void deleteById(Long id);

}
