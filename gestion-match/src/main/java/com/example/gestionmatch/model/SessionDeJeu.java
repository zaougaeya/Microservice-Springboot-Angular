package com.example.gestionmatch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "session_de_jeu")
public class SessionDeJeu {

    @Id
    private String id;

    @JsonFormat(/*pattern = "yyyy-MM-dd'T'HH:mm:ss", */timezone = "Europe/Paris")
    private LocalDateTime startDate;
    @JsonFormat(/*pattern = "yyyy-MM-dd'T'HH:mm:ss", */timezone = "Europe/Paris")
    private LocalDateTime endDate;

    private String typeMatch;
    private String terrainId;
    private String idEquipe1;
    private String idEquipe2;
    private int maxJoueurs;
    private int joueursInscrits;

    private List<String> joueurs;

    private String statut;

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getTypeMatch() { return typeMatch; }
    public void setTypeMatch(String typeMatch) { this.typeMatch = typeMatch; }

    public String getTerrainId() { return terrainId; }
    public void setTerrainId(String terrainId) { this.terrainId = terrainId; }

    public String getIdEquipe1() { return idEquipe1; }
    public void setIdEquipe1(String idEquipe1) {this.idEquipe1 = idEquipe1; }

    public String getIdEquipe2() { return idEquipe2; }
    public void setIdEquipe2(String idEquipe2) {this.idEquipe2 = idEquipe2; }

    public int getMaxJoueurs() { return maxJoueurs; }
    public void setMaxJoueurs(int maxJoueurs) { this.maxJoueurs = maxJoueurs; }

    public int getJoueursInscrits() { return joueursInscrits; }
    public void setJoueursInscrits(int joueursInscrits) { this.joueursInscrits = joueursInscrits; }

    public List<String> getJoueurs() { return joueurs; }
    public void setJoueurs(List<String> joueurs) { this.joueurs = joueurs; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
