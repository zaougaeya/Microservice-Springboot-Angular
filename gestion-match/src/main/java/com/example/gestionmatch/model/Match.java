package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "matchs")
public class Match {

    @Id
    private String id;

    private Date date;
    private Date startDate; // Date de début du match
    private Date endDate;   // Date de fin du match

    private String type; // Renommé de title vers type (champ ajouté)

    @Transient
    private Equipe equipe1; // Première équipe
    @Transient
    private Equipe equipe2; // Deuxième équipe
    @Transient
    private Terrain terrain;

    private String idEquipe1;
    private String idEquipe2;
    private String idTerrain;

    private Integer scoreEquipe1 = null; // Score initial
    private Integer scoreEquipe2 = null; // Score initial

    private Integer cartonsJaunesEquipe1 = null;
    private Integer cartonsRougesEquipe1 = null;
    private Integer cartonsJaunesEquipe2 = null;
    private Integer cartonsRougesEquipe2 = null;
    private Integer fautesEquipe1 = null;
    private Integer fautesEquipe2 = null;

    private boolean matchJoue; // Indicateur si le match est joué
}