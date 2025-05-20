package com.example.gestionmateriel.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationCreateDTO {
    private String reservedBy;
    private String materielId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
