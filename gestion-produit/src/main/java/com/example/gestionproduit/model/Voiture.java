package com.example.gestionproduit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "voitures")
public class Voiture {

    @Id
    private String idVoiture;
    
    private String modele;
    private double volumeMax;
    private String immatriculation;
    private double co2ParKm;

    @DBRef
    private Livreur livreur;

    private StatutVoiture statutVoiture = StatutVoiture.DISPONIBLE;  // Par défaut DISPONIBLE
}
