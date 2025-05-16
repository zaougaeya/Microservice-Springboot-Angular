package com.example.userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document(collection = "Affectation")
public class AffectationEquipe {
    @Id
    private String idAffectation;
    private String idEquipeMedicale;
    private String idMatch;
    private String idEvenement;
    private Date dateAffectation;

    public String getIdAffectation() {
        return idAffectation;
    }

    public void setIdAffectation(String idAffectation) {
        this.idAffectation = idAffectation;
    }

    public String getIdEquipeMedicale() {
        return idEquipeMedicale;
    }

    public void setIdEquipeMedicale(String idEquipeMedicale) {
        this.idEquipeMedicale = idEquipeMedicale;
    }

    public String getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(String idMatch) {
        this.idMatch = idMatch;
    }

    public String getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(String idEvenement) {
        this.idEvenement = idEvenement;
    }

    public Date getDateAffectation() {
        return dateAffectation;
    }

    public void setDateAffectation(Date dateAffectation) {
        this.dateAffectation = dateAffectation;
    }
}
