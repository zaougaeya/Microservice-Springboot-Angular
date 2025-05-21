package com.example.gestionproduit.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Categorie;
import com.example.gestionproduit.service.CategorieService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategorieController {
    private final CategorieService categorieService;

    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @PostMapping("/add")
    public ResponseEntity<Categorie> ajouterCategorie(@RequestBody Categorie categorie) {
        return ResponseEntity.ok(categorieService.ajouterCategorie(categorie));
    }

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<Categorie>> obtenirToutesLesCategories() {
        return ResponseEntity.ok(categorieService.obtenirToutesLesCategories());
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<Categorie> obtenirCategorieParId(@PathVariable String id) {
        Optional<Categorie> categorie = categorieService.obtenirCategorieParId(id);
        return categorie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> modifierCategorie(@PathVariable String id, @RequestBody Categorie categorie) {
        Categorie updatedCategorie = categorieService.modifierCategorie(id, categorie);
        return updatedCategorie != null ? ResponseEntity.ok(updatedCategorie) : ResponseEntity.notFound().build();
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCategorie(@PathVariable String id) {
        categorieService.supprimerCategorie(id);
        return ResponseEntity.noContent().build();
    }
}
