package com.example.gestionproduit.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
@Data
@Document(collection = "livreurs")
public class Livreur {

    @Id
    private String idLivreur;

    private String nom;
    private String prenom;
    private String numeroTelephone;
    private String email;
    
    @DBRef
     @JsonManagedReference
    private Voiture voiture;

  
    private StatutLivreur statutLivreur;
 @DBRef
    @JsonManagedReference
private List<Commande> commandes = new ArrayList<>();

    
}
