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


   @PostMapping("/ajouter")
public ResponseEntity<?> ajouterProduitAuPanier(
        @RequestParam String userId,
        @RequestParam String produitId,
        @RequestParam int quantite,
        @RequestHeader("Authorization") String token) {
    try { 
      
        Panier panier = panierService.ajouterProduitAuPanier(userId, produitId, quantite, token);

        return ResponseEntity.ok(panier);
    } catch (Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de l'ajout au panier : " + e.getMessage());
    }
}


    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<?> getPanierByUser(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        try {
          UserResponseDTO utilisateur = userClient.getCurrentUser(token);

            Panier panier = panierService.getPanierByUser(userId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }

  
    @DeleteMapping("/supprimer")
    public ResponseEntity<?> supprimerProduitDuPanier(
            @RequestParam String userId,
            @RequestParam String produitId,
            @RequestHeader("Authorization") String token) {
        try {
           
UserResponseDTO utilisateur = userClient.getCurrentUser(token);
            panierService.supprimerProduitDuPanier(userId, produitId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }


@GetMapping("/produits/{userId}")
public ResponseEntity<?> getProduitsDuPanier(
        @PathVariable String userId,
        @RequestHeader("Authorization") String token) {
    try {
   
 
UserResponseDTO utilisateur = userClient.getCurrentUser(token);

        // Récupérer le panier
        Panier panier = panierService.getPanierByUser(userId);

        // Retourner la liste des produits (CommandeProduit) avec quantités
        return ResponseEntity.ok(panier.getProduits());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la récupération des produits du panier : " + e.getMessage());
    }
}









    @DeleteMapping("/vider/{userId}")
    public ResponseEntity<?> viderPanier(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        try {
   
UserResponseDTO utilisateur = userClient.getCurrentUser(token);

            panierService.viderPanier(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur utilisateur : " + e.getMessage());
        }
    }
}
