package com.example.gestionmateriel.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private String id;
    private String userId;            // ← AJOUTÉ

    private String reservedBy;
    private String materielId;
    private String materielName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private boolean paid;
    private String terrain; // ex: "Foot", "Padel", "Tennis"
    private String materielImageUrl;

}
