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

    @DBRef
    private User user;

    @DBRef
    private Materiel materiel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ReservationStatus status;
}