package com.example.gestionproduit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document(collection = "livraisons")
public class Livraison {

    @Id
    private String idLivraison;

    @DBRef
    private Commande commande;  

    private String adresseLivraison;
    private String dateLivraison;
    private StatutLivraison statutLivraison;  
    @DBRef
    private Livreur livreur;

}
