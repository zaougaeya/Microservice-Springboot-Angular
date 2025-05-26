package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "entraineurs")
public class Entraineur {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private int age;
    @DBRef (lazy = false)
    private Equipe equipe;

}
