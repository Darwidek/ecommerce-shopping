package com.spring.ecommerce.service;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
    /**
     * Service responsible for managing file storage operations.
     *
     * This class handles saving and deleting image files on the server
     * within the Spring Boot project directory.
     */

    // Root directory where images will be stored
    private final Path root = Paths.get("uploads/image");

    /**
     * Saves an uploaded image file to the server.
     *
     * If the uploaded file is empty, a default image name is returned.
     * Otherwise, the file is stored with a unique name to avoid overwriting
     * existing files.
     *
     * @param file multipart file uploaded by the client
     * @return the stored file name, or "default.jpg" if no file is provided
     * @throws IOException if an error occurs while saving the file
     */
    public String saveImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return "default.jpg";
        }

        Files.createDirectories(root);
        String fileName = System.currentTimeMillis() + "_" +
                file.getOriginalFilename();

        Path path = root.resolve(fileName);

        Files.copy(file.getInputStream(),
                path,
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    /**
     * Deletes an image file from the server.
     *
     * This method does nothing if the filename is null or corresponds
     * to the default image.
     *
     * @param nombre name of the file to delete
     * @throws IOException if an error occurs while deleting the file
     */
    public void deleteImage(String nombre) throws IOException {

        if (nombre == null || nombre.equals("default.jpg")) {
            return;
        }

        Path path = root.resolve(nombre);

        Files.deleteIfExists(path);
    }
}

