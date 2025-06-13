package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document (collection = "equipes")
public class Equipe {
    @Id
    private String id;
    private String nameEquipe;
    private String logo;
    @DBRef
    private List<User> users = new ArrayList<>();
    private int joueurMax;
    private int joueurInscrit;
}
