package com.amatista.tienda.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String PREFIJO = "/uploads/products/";
    private static final long MAX_BYTES = 3L * 1024 * 1024;
    private static final Map<String, String> TIPOS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp");

    private final Path dir;

    public ImageStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.dir = Paths.get(uploadDir, "products").toAbsolutePath().normalize();
        Files.createDirectories(dir);
    }

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("La imagen supera los 3 MB.");
        }
        String extension = TIPOS.get(file.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("Formato no permitido. Usa JPG, PNG o WEBP.");
        }

        String nombre = UUID.randomUUID() + extension;
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
        }
        return PREFIJO + nombre;
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith(PREFIJO)) {
            return;
        }
        String nombre = Paths.get(imageUrl).getFileName().toString();
        try {
            Files.deleteIfExists(dir.resolve(nombre));
        } catch (IOException ignorado) {
            // si no se puede borrar el archivo, el producto se actualiza igual
        }
    }
}