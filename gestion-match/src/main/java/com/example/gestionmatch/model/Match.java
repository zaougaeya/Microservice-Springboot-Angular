package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Data
@Document(collection = "matchs")
public class Match {

    @Id
    private String id;

    private Date date;
    private Date  startDate;
    private Date endDate;

    private String type;

    @Transient
    private Equipe equipe1;
    @Transient
    private Equipe equipe2;
    @Transient
    private Terrain terrain;

    private String idEquipe1;
    private String idEquipe2;
    private String idTerrain;

    private Integer scoreEquipe1 = null;
    private Integer scoreEquipe2 = null;

    private Integer cartonsJaunesEquipe1 = null;
    private Integer cartonsRougesEquipe1 = null;
    private Integer cartonsJaunesEquipe2 = null;
    private Integer cartonsRougesEquipe2 = null;
    private Integer fautesEquipe1 = null;
    private Integer fautesEquipe2 = null;
    private int joueurMax;
    private int joueurInscrit1;
    private int joueurInscrit2;
    private boolean matchJoue;
}