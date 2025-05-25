package com.example.gestionmatch.Repository;

import com.example.userservice.model.SessionDeJeu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionDeJeuRepository extends MongoRepository<SessionDeJeu, String> {

    List<SessionDeJeu> findByTypeMatch(String typeMatch);
    List<SessionDeJeu> findByStatut(String statut);
    List<SessionDeJeu> findByTerrainId(String terrainId);
    @Query("{ 'startDate': { $gte: ?0 }, 'endDate': { $lte: ?1 }, 'typeMatch': { $regex: ?2, $options: 'i' } }")
    List<SessionDeJeu> findByDateAndType(LocalDateTime dateDebut, LocalDateTime dateFin, String typeMatch);


}
