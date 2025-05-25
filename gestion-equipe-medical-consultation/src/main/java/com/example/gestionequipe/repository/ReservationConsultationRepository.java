package com.example.userservice.repository;


import com.example.userservice.model.ReservationConsultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationConsultationRepository extends MongoRepository<ReservationConsultation, String> {
    Optional<ReservationConsultation> findByCreneauIdAndMedecinId(String creneauId, String medecinId);
    @Query("{ 'medecinId': ?0, $or: [ { 'dateDebut': { $lt: ?2 }, 'dateFin': { $gt: ?1 } } ] }")
    boolean existsByMedecinIdAndDateOverlap(String medecinId, LocalDateTime start, LocalDateTime end);

}
