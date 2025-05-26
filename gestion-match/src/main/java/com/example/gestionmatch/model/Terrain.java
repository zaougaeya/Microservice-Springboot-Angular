package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "terrain")
public class Terrain {
    @Id
    private String id;
    private String name;
    private String adresse;
    private String type;
    @DBRef
    private List<Match> matchsReservés;
}
