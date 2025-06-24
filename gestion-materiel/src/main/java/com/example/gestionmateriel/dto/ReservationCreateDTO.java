package com.example.gestionmateriel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationCreateDTO {
    private String materielId;
    private String terrain;
    private int quantity;
    private String note;
    private String startDate;
    private String endDate;
    private String reservedBy;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
