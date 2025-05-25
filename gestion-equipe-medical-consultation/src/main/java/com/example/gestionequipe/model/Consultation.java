package com.example.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Consultation")
public class Consultation {

    @Id
    private String id;

    private String userId; // store user ID only

    private String equipeMedicaleId; // store equipe ID only
    private String reservationId;

    private Date dateConsultation;
    private String rapport;

    private List<FeedbackConsultation> feedbacks;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getEquipeMedicaleId() {
        return equipeMedicaleId;
    }

    public void setEquipeMedicaleId(String equipeMedicaleId) {
        this.equipeMedicaleId = equipeMedicaleId;
    }

    public Date getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(Date dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    public List<FeedbackConsultation> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackConsultation> feedbacks) {
        this.feedbacks = feedbacks;
    }

}
