package com.example.gestionproduit.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.service.ProduitService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:4200")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    // 🔹 Ajouter un produit
    @PostMapping
    public ResponseEntity<Produit> ajouterProduit(@RequestBody Produit produit) {
        Produit nouveauProduit = produitService.ajouterProduit(produit);
        return ResponseEntity.ok(nouveauProduit);
    }

    // 🔹 Récupérer tous les produits
    @GetMapping
    public ResponseEntity<List<Produit>> obtenirTousLesProduits() {
        return ResponseEntity.ok(produitService.obtenirTousLesProduits());
    }

    // 🔹 Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> obtenirProduitParId(@PathVariable String id) {
        Optional<Produit> produit = produitService.obtenirProduitParId(id);
        return produit.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> mettreAJourProduit(@PathVariable String id, @RequestBody Produit produit) {
        try {
            Produit produitMisAJour = produitService.mettreAJourProduit(id, produit);
            return ResponseEntity.ok(produitMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable String id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }
    // 🔹 Récupérer les produits par catégorie
@GetMapping("/categorie/{idCategorie}")
public ResponseEntity<List<Produit>> obtenirProduitsParCategorie(@PathVariable String idCategorie) {
    return ResponseEntity.ok(produitService.obtenirProduitsParCategorie(idCategorie));
}

// 🔹 Récupérer les produits disponibles
@GetMapping("/disponibles")
public ResponseEntity<List<Produit>> obtenirProduitsDisponibles() {
    return ResponseEntity.ok(produitService.obtenirProduitsDisponibles());
}


@GetMapping("/note/{note}")
public ResponseEntity<List<Produit>> obtenirProduitsAvecNoteSuperieure(@PathVariable double note) {
    return ResponseEntity.ok(produitService.obtenirProduitsAvecNoteSuperieure(note));
}

}