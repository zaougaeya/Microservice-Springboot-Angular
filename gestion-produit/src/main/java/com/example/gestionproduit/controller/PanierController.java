package com.example.gestionproduit.controller;

import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.service.PanierService;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/panier")
@CrossOrigin(origins = "*")
public class PanierController {

    private final PanierService panierService;
    private final UserClient userClient;

    public PanierController(PanierService panierService, UserClient userClient) {
        this.panierService = panierService;
        this.userClient = userClient;
    }

    /**
     * Endpoint pour ajouter un produit au panier.
     * Exemple d'appel : POST /api/panier/ajouter?userId=USER_ID&produitId=PROD_ID&quantite=2
     */
   @PostMapping("/ajouter")
public ResponseEntity<?> ajouterProduitAuPanier(
        @RequestParam String userId,
        @RequestParam String produitId,
        @RequestParam int quantite,
        @RequestHeader("Authorization") String token) {
    try {
        // Vérifie que l'utilisateur existe (via gestion-user)
        UserResponseDTO utilisateur = userClient.getUserById(userId, token);

        // Appel du service avec token transmis
        Panier panier = panierService.ajouterProduitAuPanier(userId, produitId, quantite, token);

        return ResponseEntity.ok(panier);
    } catch (Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de l'ajout au panier : " + e.getMessage());
    }
}


    /**
     * Endpoint pour obtenir le panier d'un utilisateur.
     * Exemple d'appel : GET /api/panier/utilisateur/USER_ID
     */
    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<?> getPanierByUser(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        try {
            // Vérification utilisateur
            UserResponseDTO utilisateur = userClient.getUserById(userId, token);

            Panier panier = panierService.getPanierByUser(userId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }

    /**
     * Endpoint pour supprimer un produit du panier.
     * Exemple d'appel : DELETE /api/panier/supprimer?userId=USER_ID&produitId=PROD_ID
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<?> supprimerProduitDuPanier(
            @RequestParam String userId,
            @RequestParam String produitId,
            @RequestHeader("Authorization") String token) {
        try {
            userClient.getUserById(userId, token); // Vérification

            panierService.supprimerProduitDuPanier(userId, produitId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }

    /**
     * Endpoint pour vider complètement le panier d'un utilisateur.
     * Exemple d'appel : DELETE /api/panier/vider/USER_ID
     */
    @DeleteMapping("/vider/{userId}")
    public ResponseEntity<?> viderPanier(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        try {
            userClient.getUserById(userId, token); // Vérification

            panierService.viderPanier(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }
}
