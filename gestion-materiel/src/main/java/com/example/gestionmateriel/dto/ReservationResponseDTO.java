package com.example.gestionmateriel.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private String id;
    private String reservedBy;
    private String materielId;
    private String materielName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}
