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

    // Optionnel : stocker un identifiant client ou une référence simple, car pas de gestion de User
    private String reservedBy; // par exemple : un nom ou ID manuel

    @DBRef
    private Materiel materiel;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private ReservationStatus status = ReservationStatus.PENDING;
}
