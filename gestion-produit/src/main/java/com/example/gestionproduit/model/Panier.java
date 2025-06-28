package com.example.gestionproduit.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "panier")
public class Panier {

    @Id
    private String id;

    private String userId;

 
    private String nomUtilisateur;
    private String emailUtilisateur;

    private List<CommandeProduit> produits = new ArrayList<>();

    private double total;
    private double montantTotal;
    private double fraisLivraison;
    private double montantReduction;
    private double totalTTC;

    public void ajouterProduit(CommandeProduit produit) {
        this.produits.add(produit);
        calculerMontants();
    }

    public void supprimerProduit(CommandeProduit produit) {
        this.produits.remove(produit);
        calculerMontants();
    }

    public void calculerMontants() {
        double sousTotal = produits.stream()
                                   .mapToDouble(CommandeProduit::calculerPrixTotal)
                                   .sum();
        this.montantTotal = sousTotal;
        this.fraisLivraison = 8.0;
        this.montantReduction = 0.0;
        this.totalTTC = sousTotal + fraisLivraison - montantReduction;
    }
}
