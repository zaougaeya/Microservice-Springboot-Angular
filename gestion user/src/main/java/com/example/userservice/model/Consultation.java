package com.example.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Consultation")
public class Consultation {

    @Id
    private String id;

    @DBRef
    private User user; // The patient who reserved

    @DBRef
    private EquipeMedicale equipeMedicale; // Linked medical team

    private Date dateConsultation;
    private String rapport;

    @DBRef
    private List<FeedbackConsultation> feedbacks; // List of feedbacks for this consultation

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EquipeMedicale getEquipeMedicale() {
        return equipeMedicale;
    }

    public void setEquipeMedicale(EquipeMedicale equipeMedicale) {
        this.equipeMedicale = equipeMedicale;
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
