package com.example.gestionproduit.controller;

import com.example.gestionproduit.model.CategorieAjoutPanier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.gestionproduit.service.CategorieStatsService;
import com.example.gestionproduit.service.HistoriqueAchatsService;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommandation")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final CategorieStatsService categorieStatsService;
    private final ProduitRepository produitRepository;
    private final UserClient userClient;
     private final HistoriqueAchatsService historiqueAchatsService; 

    public RecommendationController(CategorieStatsService categorieStatsService,
                                    ProduitRepository produitRepository,
                                    UserClient userClient,
                                    HistoriqueAchatsService historiqueAchatsService
                                    ) {
        this.categorieStatsService = categorieStatsService;
        this.produitRepository = produitRepository;
        this.userClient = userClient;
        this.historiqueAchatsService= historiqueAchatsService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> recommanderProduits(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {

        try {
            
            UserResponseDTO utilisateur = userClient.getCurrentUser(token);

            
            if (!utilisateur.id().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit : utilisateur non autorisé.");
            }

            List<CategorieAjoutPanier> topCategories =
                    categorieStatsService.getTopCategoriesForUser(userId);

            if (topCategories.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

        
            List<String> topCategorieNoms = topCategories.stream()
                    .limit(2)
                    .map(CategorieAjoutPanier::getNomCategorie)
                    .collect(Collectors.toList());

          
            List<Produit> produitsRecommandes = produitRepository.findAll().stream()
                    .filter(p -> p.getCategorie() != null &&
                                 topCategorieNoms.contains(p.getCategorie().getNom()))
                    .limit(6)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(produitsRecommandes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la recommandation : " + e.getMessage());
        }
    }



 @GetMapping("/historique/{userId}")
    public ResponseEntity<?> recommanderProduitsParHistorique(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {

        try {
          
            UserResponseDTO utilisateur = userClient.getCurrentUser(token);
            if (utilisateur == null || !utilisateur.id().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit : utilisateur non autorisé.");
            }

            List<String> topCategoriesAchetees = historiqueAchatsService.getTopCategoriesFromPurchases(userId);

            if (topCategoriesAchetees.isEmpty()) {
                
                return ResponseEntity.ok(Collections.emptyList());
            }

            
            List<String> topCategorieNomsPourRecommandation = topCategoriesAchetees.stream()
                    .limit(2)
                    .collect(Collectors.toList());


            List<Produit> produitsRecommandes = produitRepository.findAll().stream()
                    .filter(p -> p.getCategorie() != null &&
                                 p.getCategorie().getNom() != null && // Ajoutez cette vérification pour la robustesse
                                 topCategorieNomsPourRecommandation.contains(p.getCategorie().getNom()))
                    .limit(6) 
                    .collect(Collectors.toList());

            return ResponseEntity.ok(produitsRecommandes);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recommandation par historique: " + e.getMessage());
     
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la recommandation par historique : " + e.getMessage());
        }
    }
}










































