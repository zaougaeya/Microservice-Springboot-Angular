package com.example.gestionproduit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("alertes_stock")
@Data
public class AlerteStock {
    @Id
    private String id;
    private String produitId;
    private String emailClient;
    private boolean notifie;
}
