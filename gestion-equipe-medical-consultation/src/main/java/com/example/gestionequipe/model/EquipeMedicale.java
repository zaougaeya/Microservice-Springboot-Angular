package com.example.userservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "EquipeMedicale")

public class EquipeMedicale {

    @Id
    private String id;

    private String nomEquipeMedicale;
    private String descEquipeMedicale;


    private List<User> membres; // List of Users


    private List<Consultation> consultations; // List of Consultations linked to the team (optional but nice!)

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomEquipeMedicale() {
        return nomEquipeMedicale;
    }

    public void setNomEquipeMedicale(String nomEquipeMedicale) {
        this.nomEquipeMedicale = nomEquipeMedicale;
    }

    public String getDescEquipeMedicale() {
        return descEquipeMedicale;
    }

    public void setDescEquipeMedicale(String descEquipeMedicale) {
        this.descEquipeMedicale = descEquipeMedicale;
    }

    public List<User> getMembres() {
        return membres;
    }

    public void setMembres(List<User> membres) {
        this.membres = membres;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }
}
