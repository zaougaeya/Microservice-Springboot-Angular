// src/main/java/com/example/userservice/controller/MaterielController.java
package com.example.gestionmateriel.controller;

import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.service.MaterielService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/materiels")
@CrossOrigin(origins = "http://localhost:4200")
public class MaterielController {

    @Autowired
    private MaterielService materielService;

    @GetMapping
    public ResponseEntity<List<Materiel>> getAllMateriel() {
        return ResponseEntity.ok(materielService.getAllMateriel());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getAllMaterielPaginatedAndSearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "name") String sortColumn,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Materiel> materielPage = materielService.getAllMaterielPaginatedAndSearch(pageable, searchTerm, sortColumn, sortDirection);

            Map<String, Object> response = new HashMap<>();
            response.put("content", materielPage.getContent());
            response.put("totalPages", materielPage.getTotalPages());
            response.put("totalElements", materielPage.getTotalElements());
            response.put("size", materielPage.getSize());
            response.put("number", materielPage.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materiel> getMaterielById(@PathVariable String id) {
        return materielService.getMaterielById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Materiel> createMateriel(@RequestBody Materiel materiel) {
        Materiel createdMateriel = materielService.createMateriel(materiel);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMateriel.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdMateriel);
    }

    @PostMapping("/upload")
    public ResponseEntity<Materiel> createMaterielWithImage(@RequestParam("image") MultipartFile file,
                                                            @RequestParam("name") String name,
                                                            @RequestParam(value = "sportType", required = false) String sportType,
                                                            @RequestParam("quantity") int quantity,
                                                            @RequestParam(value = "color", required = false) String color,
                                                            @RequestParam("state") String state,
                                                            @RequestParam(value = "price", required = false) Double price){

        try {
            Materiel materiel = new Materiel();
            materiel.setName(name);
            materiel.setSportType(sportType);
            materiel.setQuantity(quantity);
            materiel.setColor(color);
            materiel.setState(state);
            materiel.setPrice(price);

            // Sauvegarde l'image sur Cloudinary
            String imageUrl = materielService.saveImage(file);
            materiel.setImageUrl(imageUrl);

            // Sauvegarde le materiel dans la base de données
            Materiel createdMateriel = materielService.createMateriel(materiel);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdMateriel.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdMateriel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Échec du téléchargement de l'image", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materiel> updateMateriel(@PathVariable String id, @RequestBody Materiel materiel) {
        Optional<Materiel> existingMateriel = materielService.getMaterielById(id);
        if (existingMateriel.isPresent()) {
            materiel.setId(id);
            Materiel updatedMateriel = materielService.updateMateriel(id, materiel);
            return ResponseEntity.ok(updatedMateriel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateriel(@PathVariable String id) {
        materielService.deleteMateriel(id);
        return ResponseEntity.noContent().build();
    }
}