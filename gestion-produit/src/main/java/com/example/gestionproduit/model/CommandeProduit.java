package com.example.gestionproduit.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Document(collection = "commande_produit") // Nom de la collection dans MongoDB
public class CommandeProduit {

    @Id
    private String id; // Identifiant unique pour chaque enregistrement de produit dans la commande

    @DBRef
     @JsonIgnore
    private Commande commande; 

    @DBRef
    private Produit produit; 

    private int quantite; 
    private double prixUnitaire; 
    private double prixTotal; 

    public double calculerPrixTotal() {
        return this.quantite * this.prixUnitaire;
    }
}

