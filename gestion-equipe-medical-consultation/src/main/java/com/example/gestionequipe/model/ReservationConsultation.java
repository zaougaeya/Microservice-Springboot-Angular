package com.example.gestionequipe.model;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.util.Date;

@Document(collection = "ReservationConsultation")
public class ReservationConsultation {

    @Id
    private String id;

    private Date dateConsultation;
    private String equipeMedicaleId;
    private String medecinId;
    private String patientId;
    private String specialite; //profession retrieved mel user
    private String statutConsultation; // e.g., "A_VENIR", "FINIE", "ANNULEE"
    private String motifConsultation;  // Reason for the consultation checkup, pain
    @Setter
    private String moyenCommunication; // e.g., "Présentiel", "Visio", "Téléphone"
    private String isUrgent; //or or no
    private Duration dureeConsultation; // Duration of the consultation
    private String consultationId;
    private String creneauId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(Date dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getEquipeMedicaleId() {
        return equipeMedicaleId;
    }

    public void setEquipeMedicaleId(String equipeMedicaleId) {
        this.equipeMedicaleId = equipeMedicaleId;
    }

    public String getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(String medecinId) {
        this.medecinId = medecinId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getStatutConsultation() {
        return statutConsultation;
    }

    public void setStatutConsultation(String statutConsultation) {
        this.statutConsultation = statutConsultation;
    }

    public String getMotifConsultation() {
        return motifConsultation;
    }

    public void setMotifConsultation(String motifConsultation) {
        this.motifConsultation = motifConsultation;
    }

    public String getMoyenCommunication() {
        return moyenCommunication;
    }

    public void setMoyenCommunication(String moyenCommunication) {
        this.moyenCommunication = moyenCommunication;
    }

    public String getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(String isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Duration getDureeConsultation() {
        return dureeConsultation;
    }

    public void setDureeConsultation(Duration dureeConsultation) {
        this.dureeConsultation = dureeConsultation;
    }

    public String getCreneauId() {
        return creneauId;
    }

    public void setCreneauId(String creneauId) {
        this.creneauId = creneauId;
    }
}