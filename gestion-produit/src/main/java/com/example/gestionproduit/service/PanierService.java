package com.example.gestionproduit.service;

import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Panier;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.PanierRepository;
import com.example.gestionproduit.repository.ProduitRepository;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PanierService {

    private final PanierRepository panierRepository;
    private final ProduitRepository produitRepository;
    private final UserClient userClient;
private final CategorieStatsService categorieStatsService;

    public PanierService(PanierRepository panierRepository,
                         ProduitRepository produitRepository,
                         UserClient userClient,
                         CategorieStatsService categorieStatsService) {
        this.panierRepository = panierRepository;
        this.produitRepository = produitRepository;
        this.userClient = userClient;
          this.categorieStatsService = categorieStatsService;

    }

    public Panier ajouterProduitAuPanier(String userId, String produitId, int quantite, String token) {

        // 1️⃣ Vérification du produit
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + produitId));
                String nomCategorie = produit.getCategorie().getNom();
categorieStatsService.enregistrerAjout(userId, nomCategorie);
        if (produit.getQuantiteEnStock() < quantite) {
            throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
        }

     UserResponseDTO user = userClient.getCurrentUser(token);

if (!user.id().equals(userId)) {
    throw new SecurityException("Accès interdit : l'utilisateur ne correspond pas au token.");
}
      

        // 3️⃣ Création du produit commandé
        CommandeProduit cp = new CommandeProduit();
        cp.setProduit(produit);
        cp.setQuantite(quantite);
        cp.setPrixUnitaire(produit.getPrix());
        cp.setPrixTotal(cp.getPrixUnitaire() * quantite);

        // 4️⃣ Récupération ou création du panier
        Panier panier = panierRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Panier p = new Panier();
                    p.setUserId(userId);
                    return p;
                });

        // 5️⃣ Produit déjà dans le panier ?
        boolean produitExistant = false;
        for (CommandeProduit cpExistant : panier.getProduits()) {
            if (cpExistant.getProduit().getId().equals(produitId)) {
                int nouvelleQuantite = cpExistant.getQuantite() + quantite;

                if (produit.getQuantiteEnStock() < nouvelleQuantite) {
                    throw new RuntimeException("Stock insuffisant pour la quantité totale demandée");
                }

                cpExistant.setQuantite(nouvelleQuantite);
                cpExistant.setPrixTotal(cpExistant.getPrixUnitaire() * nouvelleQuantite);
                produitExistant = true;
                break;
            }
        }

        if (!produitExistant) {
            panier.getProduits().add(cp);
        }

        // 6️⃣ Mise à jour du stock
        produit.setQuantiteEnStock(produit.getQuantiteEnStock() - quantite);
        produitRepository.save(produit);

        // 7️⃣ Recalcul et sauvegarde
        panier.calculerMontants();
        panier.setNomUtilisateur(user.nomuser());
    panier.setEmailUtilisateur(user.mailuser());
        return panierRepository.save(panier);
    }

    public Panier getPanierByUser(String userId) {
        return panierRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour l'utilisateur avec ID : " + userId));
    }

    public void supprimerProduitDuPanier(String userId, String produitId) {
        Panier panier = getPanierByUser(userId);
        panier.getProduits().removeIf(cp -> cp.getProduit().getId().equals(produitId));
        panier.calculerMontants();
        panierRepository.save(panier);
    }

    public void viderPanier(String userId) {
        Panier panier = getPanierByUser(userId);
        panier.getProduits().clear();
        panier.calculerMontants();
        panierRepository.save(panier);
    }
}
