package com.example.gestionmateriel.model;

public enum ReservationStatus {
    PENDING,     // En attente de confirmation
    CONFIRMED,   // Confirmée manuellement ou automatiquement
    CANCELLED,   // Annulée
    COMPLETED  ,
    PAID// Terminée après la date de fin
}