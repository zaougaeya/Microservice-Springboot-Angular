package com.example.gestionmatch.repository;

import com.example.gestionmatch.model.Match;
import com.example.gestionmatch.model.SessionDeJeu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByIdEquipe1(String equipeId);
    List<Match> findByIdEquipe2(String equipeId);
    List<Match> findByIdTerrain(String terrainId);
    List<Match> findByDateBetween(Date dateD, Date dateF);
    @Query("{ 'startDate': { $gte: ?0 }, 'endDate': { $lte: ?1 }, 'type': { $regex: ?2, $options: 'i' } }")
    List<Match> findByDateAndType(Date startDate, Date endDate, String type);
    List<Match> findByMatchJoueTrue();

}
