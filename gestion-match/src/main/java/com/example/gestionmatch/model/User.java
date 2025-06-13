package com.example.gestionmatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id; // MongoDB uses String for ID

    private String nom;
    private String prenom;
    private int age;
    private String sexe;

    private String tel;
    private String adresse;
    private String email;
    private String profession;
    private Role role; // Enum field for role
    private String equipeId;

    @DBRef
    private Equipe equipe;
    private String iduser;

}
