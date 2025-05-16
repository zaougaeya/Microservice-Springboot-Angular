package com.example.gestionuser.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String nomuser;
    private String prenomuser;
    private int ageuser;
    private String phoneuser;
    private String sexeuser;
    private String mailuser;
    private String passworduser;
    private String addresseuser;

    public User() {}

    public User(String nomuser, String prenomuser, int ageuser, String phoneuser,
                String sexeuser, String mailuser, String passworduser, String addresseuser) {
        this.nomuser = nomuser;
        this.prenomuser = prenomuser;
        this.ageuser = ageuser;
        this.phoneuser = phoneuser;
        this.sexeuser = sexeuser;
        this.mailuser = mailuser;
        this.passworduser = passworduser;
        this.addresseuser = addresseuser;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomuser() {
        return nomuser;
    }

    public void setNomuser(String nomuser) {
        this.nomuser = nomuser;
    }

    public String getPrenomuser() {
        return prenomuser;
    }

    public void setPrenomuser(String prenomuser) {
        this.prenomuser = prenomuser;
    }

    public int getAgeuser() {
        return ageuser;
    }

    public void setAgeuser(int ageuser) {
        this.ageuser = ageuser;
    }

    public String getPhoneuser() {
        return phoneuser;
    }

    public void setPhoneuser(String phoneuser) {
        this.phoneuser = phoneuser;
    }

    public String getSexeuser() {
        return sexeuser;
    }

    public void setSexeuser(String sexeuser) {
        this.sexeuser = sexeuser;
    }

    public String getMailuser() {
        return mailuser;
    }

    public void setMailuser(String mailuser) {
        this.mailuser = mailuser;
    }

    public String getPassworduser() {
        return passworduser;
    }

    public void setPassworduser(String passworduser) {
        this.passworduser = passworduser;
    }

    public String getAddresseuser() {
        return addresseuser;
    }

    public void setAddresseuser(String addresseuser) {
        this.addresseuser = addresseuser;
    }
}
