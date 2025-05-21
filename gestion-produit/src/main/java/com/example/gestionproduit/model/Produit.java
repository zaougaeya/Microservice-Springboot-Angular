package com.example.gestionproduit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Date;
@Data
@Document(collection = "produits") 
public class Produit {
    @Id
    private String id; 

    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;

    @DBRef 
    private Categorie categorie; 

    private boolean disponible; 
    private String imageUrl; 
    private Date dateAjout; 
    private double note; 
    private double pourcentagePromotion;
    private GenreProduit genreProduit; 

    private StatutProduit statutProduit;

    public double getPrixAprèsPromotion() {
        if (pourcentagePromotion > 0) {
            double reduction = prix * (pourcentagePromotion / 100);
            return prix - reduction; 
        }
        return prix; 
    }
    public void setQuantiteEnStock(int quantite) {
        this.quantiteEnStock = quantite;
        this.disponible = quantite > 0;
        this.statutProduit = quantite > 0 ? StatutProduit.EN_STOCK : StatutProduit.RUPTURE_DE_STOCK;
    }
    
    

}
