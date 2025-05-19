package com.example.gestionproduit.model;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Document(collection = "commandes")
public class Commande {

    @Id
    private String idCommande; 

    private Double montantTotal; 
    private String dateCommande; 
    private Date dateAffectation;

    @DBRef
     @JsonBackReference
    private Livreur livreur; 

    @DBRef
    private User client; 

    private StatutCommande statutCommande; 
    private String adresseLivraison; 
    private String nomClient; 
    private String emailClient; 
    private String telephoneClient; 
    private String dateLivraisonPrevue;
     private List<CommandeProduit> produits;
    
}
