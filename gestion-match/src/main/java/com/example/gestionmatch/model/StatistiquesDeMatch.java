package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class StatistiquesDeMatch {

    @DBRef
    private Match match;
    @DBRef
    private StatistiqueEquipes equipe1Stat;
    @DBRef
    private StatistiqueEquipes equipe2Stat;

    private int totalButs;
    private int totalFautes;
    private int totalCartonsJaunes;
    private int totalCartonsRouges;




}
