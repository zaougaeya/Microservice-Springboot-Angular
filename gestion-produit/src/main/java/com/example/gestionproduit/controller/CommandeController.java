package com.example.gestionproduit.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.service.CommandeService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    // ✅ Créer une commande à partir du panier utilisateur
    @PostMapping("/creer/{userId}")
    public ResponseEntity<?> creerCommande(@PathVariable String userId) {
        try {
            Commande commande = commandeService.creerCommandeDepuisPanier(userId);
            return ResponseEntity.ok(commande);
        } catch (RuntimeException e) {
            String messageErreur = e.getMessage().toLowerCase();
            if (messageErreur.contains("panier est vide")) {
                return ResponseEntity.badRequest().body("Erreur : Le panier est vide.");
            } else if (messageErreur.contains("panier non trouvé")) {
                return ResponseEntity.badRequest().body("Erreur : Panier non trouvé pour l'utilisateur.");
            } else if (messageErreur.contains("utilisateur non trouvé")) {
                return ResponseEntity.badRequest().body("Erreur : Utilisateur non trouvé.");
            }
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }

    // ✅ Supprimer toutes les commandes d’un utilisateur
    @DeleteMapping("/supprimer/{userId}")
    public ResponseEntity<String> supprimerCommandesUtilisateur(@PathVariable String userId) {
        try {
            commandeService.supprimerCommandeUtilisateur(userId);
            return ResponseEntity.ok("Commandes supprimées avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
        }
    }
    @GetMapping("/all")
public ResponseEntity<?> getAllCommandes() {
    try {
        return ResponseEntity.ok(commandeService.getAllCommandes());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erreur lors de la récupération des commandes : " + e.getMessage());
    }
}

}
