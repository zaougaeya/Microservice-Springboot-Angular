package com.example.gestionproduit.service;

import com.example.gestionproduit.model.Commande;
import com.example.gestionproduit.model.CommandeProduit;
import com.example.gestionproduit.model.Produit;
import com.example.gestionproduit.repository.CommandeRepository;
import com.example.gestionproduit.repository.CommandeProduitRepository; // NOUVEL IMPORT : pour charger les CommandeProduit
import lombok.RequiredArgsConstructor; // Pour l'injection automatique via constructeur
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service 
@RequiredArgsConstructor 
public class HistoriqueAchatsService {

  
    private final CommandeRepository commandeRepository;
    private final CommandeProduitRepository commandeProduitRepository; // Nécessaire pour charger les détails des produits de commande

    /**
     
     *
     * 
     * 

     *
     * @param userId 
     * @return Une liste de noms de catégories, triée par ordre décroissant de fréquence d'achat.
     
     */
    public List<String> getTopCategoriesFromPurchases(String userId) {
        
        List<Commande> commandes = commandeRepository.findByClientId(userId);

        // Map pour stocker la fréquence (nombre total de quantités achetées) pour chaque catégorie.
        // La clé est le nom de la catégorie (String), la valeur est la fréquence (Integer).
        Map<String, Integer> categorieFrequencies = new HashMap<>();

        // 2. Parcourir chaque commande besh ye3ml l'extration des commandes
        for (Commande commande : commandes) {
          
            // Il faut les charger explicitement en utilisant commandeProduitRepository bitbi3a
            List<CommandeProduit> produitsCommande = commandeProduitRepository.findByCommande(commande);

            // verifiii el liste mich nulle
            if (produitsCommande != null) {
                // Pour chaque produit dans la commande, extraire sa catégorie et mettre à jour la fréquence.
                for (CommandeProduit cp : produitsCommande) {
                    Produit produit = cp.getProduit(); // Accéder à l'objet Produit référencé

                    // Vérifier que le produit et sa catégorie sont valides
                    if (produit != null && produit.getCategorie() != null && produit.getCategorie().getNom() != null) {
                        String nomCategorie = produit.getCategorie().getNom();
                        // Incrémenter la fréquence de la catégorie par la quantité achetée du produit.
                        // Si la catégorie n'existe pas encore dans la map, l'initialiser à la quantité actuelle.
                        categorieFrequencies.put(nomCategorie, categorieFrequencies.getOrDefault(nomCategorie, 0) + cp.getQuantite());
                    }
                }
            }
        }

        //  Trier les catégories par leur fréquence d'achat
        // Convertir la map en un Stream d'entrées (clé-valeur).
        return categorieFrequencies.entrySet().stream()
                // Trier les entrées en comparant leurs valeurs (fréquences) dans l'ordre décroissant.
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                // Mapper chaque entrée pour ne récupérer que le nom de la catégorie (la clé).
                .map(Map.Entry::getKey)
                // Collecter les noms de catégories triés dans une nouvelle liste.
                .collect(Collectors.toList());
    }

    /**
     * Récupère tous les produits uniques qui ont été achetés par un utilisateur.
     
      @param userId 
      @return Une liste de produits uniques achetés.
     */
    public List<Produit> getProduitsAchetesUniques(String userId) {
        List<Commande> commandes = commandeRepository.findByClientId(userId);
        Map<String, Produit> uniqueProduitsMap = new HashMap<>(); // Utilise une Map pour garantir l'unicité par ID

        for (Commande commande : commandes) {
            List<CommandeProduit> produitsCommande = commandeProduitRepository.findByCommande(commande);
            if (produitsCommande != null) {
                for (CommandeProduit cp : produitsCommande) {
                    Produit produit = cp.getProduit();
                    if (produit != null) {
                        uniqueProduitsMap.put(produit.getId(), produit); 
                    }
                }
            }
        }
        return new ArrayList<>(uniqueProduitsMap.values()); // Retourne les valeurs mte les produits fil map
    }
}