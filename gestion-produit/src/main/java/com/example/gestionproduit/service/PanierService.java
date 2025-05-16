package com.example.gestionproduit.service;


import org.springframework.stereotype.Service;

import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.PanierRepository;
import com.example.gestionproduit.repository.ProduitRepository;

import java.util.Optional;

@Service
public class PanierService {

    private final PanierRepository panierRepository;
    private final ProduitRepository produitRepository;

    public PanierService(PanierRepository panierRepository, ProduitRepository produitRepository) {
        this.panierRepository = panierRepository;
        this.produitRepository = produitRepository;
    }

   
    public Panier ajouterProduitAuPanier(String userId, String produitId, int quantite) {

        // 1️⃣ Vérification si le produit existe
        Optional<Produit> produitOptional = produitRepository.findById(produitId);
        if (produitOptional.isEmpty()) {
            throw new RuntimeException("Produit non trouvé avec l'ID : " + produitId);
        }
        Produit produit = produitOptional.get();
    
        // 2️⃣ Vérification du stock disponible
        if (produit.getQuantiteEnStock() < quantite) {
            throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom() 
                                       + ". Quantité demandée : " + quantite 
                                       + ", Stock disponible : " + produit.getQuantiteEnStock());
        }
    
        // 3️⃣ Création de l'objet CommandeProduit
        CommandeProduit cp = new CommandeProduit();
        cp.setProduit(produit);
        cp.setQuantite(quantite);
        cp.setPrixUnitaire(produit.getPrix());
        cp.setPrixTotal(produit.getPrix() * quantite);
    
        // 4️⃣ Récupération du panier ou création si non existant
        Panier panier = panierRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setUserId(userId);
                    return nouveauPanier;
                });
    
        // 5️⃣ Vérification si le produit est déjà dans le panier
        boolean produitExistant = false;
        for (CommandeProduit cpExistant : panier.getProduits()) {
            if (cpExistant.getProduit().getId().equals(produitId)) {
                int nouvelleQuantite = cpExistant.getQuantite() + quantite;
                
                // Vérification du stock à chaque ajout supplémentaire
                if (produit.getQuantiteEnStock() < nouvelleQuantite) {
                    throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom() 
                                               + ". Quantité totale demandée : " + nouvelleQuantite 
                                               + ", Stock disponible : " + produit.getQuantiteEnStock());
                }
                
                cpExistant.setQuantite(nouvelleQuantite);
                cpExistant.setPrixTotal(cpExistant.getPrixUnitaire() * cpExistant.getQuantite());
                produitExistant = true;
                break;
            }
        }
        
        // 6️⃣ Ajout du produit si non existant dans le panier
        if (!produitExistant) {
            panier.getProduits().add(cp);
        }
    
        // 7️⃣ Mise à jour du stock
        produit.setQuantiteEnStock(produit.getQuantiteEnStock() - quantite);
        produitRepository.save(produit);
    
        // 8️⃣ Recalcul des montants du panier et sauvegarde
        panier.calculerMontants();
        return panierRepository.save(panier);
    }
    
   

    public Panier getPanierByUser(String userId) {
        return panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur avec ID : " + userId));
    }

   
    public void supprimerProduitDuPanier(String userId, String produitId) {
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur avec ID : " + userId));
        panier.getProduits().removeIf(cp -> cp.getProduit().getId().equals(produitId));
        panier.calculerMontants();
        panierRepository.save(panier);
    }

  
    public void viderPanier(String userId) {
        Panier panier = panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur avec ID : " + userId));
        panier.getProduits().clear();
        panier.calculerMontants();
        panierRepository.save(panier);
    }
}
