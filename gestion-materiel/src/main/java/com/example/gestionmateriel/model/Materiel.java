package com.example.gestionmateriel.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "materiels")
public class Materiel {
    @Id
    private String id;
    private String name;
    private String sportType; // Nouveau champ pour le type de sport
    private String location;   // Nouveau champ pour l'emplacement du terrain
    private int quantity;
    private String color;
    private String state;
    private String imageUrl; // Nouveau champ pour l'URL de la photo
    private Double price;
    private  String noteInterne;
    private  String  reservedDates;
    private String createdByName; // pour affichage




}