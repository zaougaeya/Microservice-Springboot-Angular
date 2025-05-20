package com.example.gestionmateriel.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    // Optionnel mais nécessaire pour traçabilité : prénom/nom/email/etc.
    private String reservedBy;

    // Référence au matériel réservé
    @DBRef
    private Materiel materiel;

    // Optionnel : tu pourras rajouter un champ terrain si tu relies un terrain plus tard

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Par défaut : PENDING, changera après confirmation manuelle ou auto
    private ReservationStatus status = ReservationStatus.PENDING;

    // Optionnel mais utile pour audit/logs
    private LocalDateTime createdAt = LocalDateTime.now();
}
