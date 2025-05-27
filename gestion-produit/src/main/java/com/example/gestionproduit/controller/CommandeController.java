package com.example.gestionproduit.controller;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.service.CommandeService;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;
     private final UserClient userClient;

    // ✅ Créer une commande à partir du panier utilisateur
     // @PostMapping("/creer/{userId}")
    /*public ResponseEntity<?> creerCommande(@PathVariable String userId) {
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
*/
@PostMapping("/creer/{userId}")
    public ResponseEntity<?> creerCommande(@PathVariable String userId, @RequestHeader("Authorization") String token) {
        try {
            // 👇 Récupération des infos utilisateur depuis gestion-user
            UserResponseDTO utilisateur = userClient.getUserById(userId, token);

            Commande commande = commandeService.creerCommandeDepuisPanier(userId,token);
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

    @DeleteMapping("/supprimer-commande/{idCommande}")
public ResponseEntity<Map<String, String>> supprimerCommandeParId(@PathVariable String idCommande) {
    Map<String, String> response = new HashMap<>();
    try {
        commandeService.supprimerCommandeParId(idCommande);
        response.put("message", "Commande supprimée avec succès.");
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        response.put("error", "Erreur : " + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    } catch (Exception e) {
        response.put("error", "Erreur interne du serveur : " + e.getMessage());
        return ResponseEntity.status(500).body(response);
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
