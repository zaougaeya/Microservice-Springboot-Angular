package com.example.gestionproduit.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.service.PanierService;

@RestController
@RequestMapping("/api/panier")
@CrossOrigin(origins = "*")
public class PanierController {

    private final PanierService panierService;

    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    /**
     * Endpoint pour ajouter un produit au panier.
     * Exemple d'appel : POST /api/panier/ajouter?userId=USER_ID&produitId=PROD_ID&quantite=2
     */
    @PostMapping("/ajouter")
    public ResponseEntity<Panier> ajouterProduitAuPanier(
            @RequestParam String userId,
            @RequestParam String produitId,
            @RequestParam int quantite) {
        Panier panier = panierService.ajouterProduitAuPanier(userId, produitId, quantite);
        return ResponseEntity.ok(panier);
    }

    /**
     * Endpoint pour obtenir le panier d'un utilisateur.
     * Exemple d'appel : GET /api/panier/utilisateur/USER_ID
     */
    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<Panier> getPanierByUser(@PathVariable String userId) {
        Panier panier = panierService.getPanierByUser(userId);
        return ResponseEntity.ok(panier);
    }

    /**
     * Endpoint pour supprimer un produit du panier.
     * Exemple d'appel : DELETE /api/panier/supprimer?userId=USER_ID&produitId=PROD_ID
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<Void> supprimerProduitDuPanier(
            @RequestParam String userId,
            @RequestParam String produitId) {
        panierService.supprimerProduitDuPanier(userId, produitId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint pour vider complètement le panier d'un utilisateur.
     * Exemple d'appel : DELETE /api/panier/vider/USER_ID
     */
    @DeleteMapping("/vider/{userId}")
    public ResponseEntity<Void> viderPanier(@PathVariable String userId) {
        panierService.viderPanier(userId);
        return ResponseEntity.noContent().build();
    }
}
