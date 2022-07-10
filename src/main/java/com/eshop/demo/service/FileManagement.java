package com.eshop.demo.service;

import com.eshop.demo.exceptions.EntityStateException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileManagement {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException ioe) {
            throw new EntityStateException("Could not create directory image file");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new EntityStateException("Could not save image file: " + fileName);
        }
    }

}
