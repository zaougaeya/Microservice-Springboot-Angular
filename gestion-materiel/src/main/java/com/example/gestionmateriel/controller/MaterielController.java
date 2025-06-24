package com.example.gestionmateriel.controller;

import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.service.MaterielService;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/materiels")
@CrossOrigin(origins = "http://localhost:4200")
public class MaterielController {

    private final MaterielService materielService;
    private final UserClient userClient;

    @Autowired
    public MaterielController(MaterielService materielService, UserClient userClient) {
        this.materielService = materielService;
        this.userClient = userClient;
    }

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
        Materiel materiel = materielService.getMaterielById(id);
        return ResponseEntity.ok(materiel);
    }

    @PostMapping
    public ResponseEntity<Materiel> createMateriel(@RequestBody Materiel materiel,
                                                   @RequestHeader("Authorization") String token) {
        try {
            UserResponseDTO user = userClient.getCurrentUser(token);
            // materiel.setCreatedByUserId(user.id()); // Optional
            Materiel createdMateriel = materielService.createMateriel(materiel);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdMateriel.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdMateriel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non valide", e);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Materiel> createMaterielWithImage(@RequestParam("image") MultipartFile file,
                                                            @RequestParam("name") String name,
                                                            @RequestParam(value = "sportType", required = false) String sportType,
                                                            @RequestParam("quantity") int quantity,
                                                            @RequestParam(value = "color", required = false) String color,
                                                            @RequestParam("state") String state,
                                                            @RequestParam("price") Double price,
                                                            @RequestParam(value = "noteInterne", required = false) String noteInterne,
                                                            @RequestHeader("Authorization") String token) {
        try {
            // TEMPORAIRE : désactiver la récupération de l'utilisateur
            // UserResponseDTO user = userClient.getCurrentUser(token);

            Materiel materiel = new Materiel();
            materiel.setName(name);
            materiel.setSportType(sportType);
            materiel.setQuantity(quantity);
            materiel.setColor(color);
            materiel.setState(state);
            materiel.setPrice(price);
            materiel.setNoteInterne(noteInterne);
            // materiel.setCreatedByUserId(user.id()); // déjà commenté

            String imageUrl = materielService.saveImage(file);
            materiel.setImageUrl(imageUrl);

            Materiel createdMateriel = materielService.createMateriel(materiel);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdMateriel.getId())
                    .toUri();
            return ResponseEntity.created(location).body(createdMateriel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Échec du téléchargement de l'image", e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Materiel> updateMateriel(@PathVariable String id, @RequestBody Materiel materiel) {
        Optional<Materiel> existingMateriel = materielService.getMaterielByIdOptional(id);
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
