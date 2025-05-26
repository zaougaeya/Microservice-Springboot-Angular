package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "joueurs")
public class Joueur {
    @Id
    private String id;
    private String nom;
    private String prenom;
    private int age;
    private String position;  // Exemple : Attaquant, Défenseur...
    private int numero;  // Numéro du maillot

    private String equipeId;
    @DBRef
    private Equipe equipe;

}
