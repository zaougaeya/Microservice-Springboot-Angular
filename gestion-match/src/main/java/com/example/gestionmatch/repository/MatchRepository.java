package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByIdEquipe1(String equipeId);
    List<Match> findByIdEquipe2(String equipeId);
    List<Match> findByIdTerrain(String terrainId);
    List<Match> findByDateBetween(Date dateD, Date dateF);


}
