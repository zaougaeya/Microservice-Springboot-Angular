package com.example.gestionproduit.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "categories") 
public class Categorie {

    @Id
    private String id; 

    private String nom; 
    private String description; 

    @DBRef 
    private List<Produit> produits; 
}
