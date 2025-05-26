package com.example.gestionmatch.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Méthode pour télécharger une image sur Cloudinary
    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("resource_type", "auto");

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        return (String) uploadResult.get("url");  // Retourne l'URL de l'image téléchargée
    }
}
