package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.Terrain;
import com.example.gestionmatch.service.TerrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/terrains")
@CrossOrigin(origins = "http://localhost:4200")
public class TerrainController {

    private final TerrainService terrainService;

    // Injection des dépendances via le constructeur
    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @PostMapping
    public ResponseEntity<Terrain> createTerrain(@RequestBody Terrain terrain) {
        try {
            Terrain createdTerrain = terrainService.createTerrain(terrain);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTerrain);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // Récupérer tous les terrains
    @GetMapping
    public List<Terrain> getAllTerrains() {
        return terrainService.getAllTerrains();
    }

    // Récupérer un terrain par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable String id) {
        Terrain result = terrainService.getTerrainById(id);
        if (result != null) {
            return ResponseEntity.ok(result);  // Si l'équipe est trouvée, retour 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retour 404 Not Found si l'équipe n'est pas trouvée
        }
    }


    // Mettre à jour un terrain
    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable String id, @RequestBody Terrain terrainDetails) {
        Optional<Terrain> updatedTerrain = terrainService.updateTerrain(id, terrainDetails);

        return updatedTerrain
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Supprimer un terrain
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable String id) {
        boolean deleted = terrainService.deleteTerrain(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }


}


