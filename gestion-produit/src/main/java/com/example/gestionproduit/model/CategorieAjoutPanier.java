package com.example.gestionproduit.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categorie_ajout_panier")
@Data
public class CategorieAjoutPanier {
    @Id
    private String id;
    private String userId;
    private String nomCategorie;
    private int nombreAjouts;
}