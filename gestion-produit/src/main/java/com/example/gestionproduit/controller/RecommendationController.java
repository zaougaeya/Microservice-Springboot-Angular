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
            // Récupérer les infos utilisateur via token
            UserResponseDTO utilisateur = userClient.getCurrentUser(token);

            // Vérifier que l'utilisateur correspond bien au userId en path
            if (!utilisateur.id().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit : utilisateur non autorisé.");
            }

            // Récupérer les catégories les plus ajoutées par cet utilisateur
            List<CategorieAjoutPanier> topCategories =
                    categorieStatsService.getTopCategoriesForUser(userId);

            if (topCategories.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Prendre les noms des 2 catégories les plus populaires
            List<String> topCategorieNoms = topCategories.stream()
                    .limit(2)
                    .map(CategorieAjoutPanier::getNomCategorie)
                    .collect(Collectors.toList());

            // Filtrer les produits appartenant à ces catégories
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
            // Vérification de l'utilisateur via token (logique standard de sécurité)
            UserResponseDTO utilisateur = userClient.getCurrentUser(token);
            if (utilisateur == null || !utilisateur.id().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès interdit : utilisateur non autorisé.");
            }

            // ** POINT CLÉ : Utilisation du nouveau service HistoriqueAchatsService **
            List<String> topCategoriesAchetees = historiqueAchatsService.getTopCategoriesFromPurchases(userId);

            if (topCategoriesAchetees.isEmpty()) {
                // Si l'historique d'achats est vide pour cet utilisateur, on renvoie une liste vide.
                // À ce stade, vous pourriez décider de renvoyer des produits "populaires" ou généraux.
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Prendre les noms des 2 catégories les plus populaires de l'historique d'achats
            // Vous pouvez ajuster le 'limit' si vous voulez plus ou moins de catégories
            List<String> topCategorieNomsPourRecommandation = topCategoriesAchetees.stream()
                    .limit(2)
                    .collect(Collectors.toList());

            // Filtrer tous les produits disponibles pour trouver ceux qui appartiennent à ces catégories
            List<Produit> produitsRecommandes = produitRepository.findAll().stream()
                    .filter(p -> p.getCategorie() != null &&
                                 p.getCategorie().getNom() != null && // Ajoutez cette vérification pour la robustesse
                                 topCategorieNomsPourRecommandation.contains(p.getCategorie().getNom()))
                    .limit(6) // Limitez le nombre de produits recommandés à afficher
                    .collect(Collectors.toList());

            return ResponseEntity.ok(produitsRecommandes);

        } catch (Exception e) {
            System.err.println("Erreur lors de la recommandation par historique: " + e.getMessage());
            // Loggez l'exception pour le débogage côté serveur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la recommandation par historique : " + e.getMessage());
        }
    }
}










































