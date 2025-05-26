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
    private String position;
    private int numero;

    private String equipeId;
    @DBRef
    private Equipe equipe;

}
