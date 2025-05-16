// src/main/java/com/example/userservice/service/MaterielService.java
package com.example.gestionmateriel.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.repository.MaterielRepository;
import com.example.userservice.model.Materiel;
import com.example.userservice.repository.MaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MaterielService {

    @Autowired
    private MaterielRepository materielRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<Materiel> getAllMateriel() {
        return materielRepository.findAll();
    }

    public Page<Materiel> getAllMaterielPaginated(Pageable pageable) {
        return materielRepository.findAll(pageable);
    }

    public Page<Materiel> getAllMaterielPaginatedAndSearch(Pageable pageable, String searchTerm, String sortColumn, String sortDirection) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            return materielRepository.searchAllFields(searchTerm, pageable);
        }
        return materielRepository.findAll(pageable);
    }

    public Optional<Materiel> getMaterielById(String id) {
        return materielRepository.findById(id);
    }

    public Materiel createMateriel(Materiel materiel) {
        return materielRepository.save(materiel);
    }

    public Materiel updateMateriel(String id, Materiel materiel) {
        materiel.setId(id);
        return materielRepository.save(materiel);
    }

    public void deleteMateriel(String id) {
        materielRepository.deleteById(id);
    }

    public String saveImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url"); // retourne l’URL publique Cloudinary
        } catch (Exception e) {
            throw new RuntimeException("Échec du téléversement d’image sur Cloudinary", e);
        }
    }

    public Materiel createMaterielWithImage(Materiel materiel, MultipartFile file) {
        String imageUrl = saveImage(file);
        materiel.setImageUrl(imageUrl);
        return materielRepository.save(materiel);
    }

    public Materiel updateMaterielWithImage(String id, Materiel materiel, MultipartFile file) {
        materiel.setId(id);
        String imageUrl = saveImage(file);
        materiel.setImageUrl(imageUrl);
        return materielRepository.save(materiel);
    }
}