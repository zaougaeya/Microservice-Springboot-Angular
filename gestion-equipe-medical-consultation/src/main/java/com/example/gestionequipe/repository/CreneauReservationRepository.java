package com.example.gestionequipe.repository;

import com.example.gestionequipe.model.CreneauReservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface CreneauReservationRepository extends MongoRepository<CreneauReservation, String> {
    List<CreneauReservation> findByMedecinIdAndDate(String medecinId, LocalDate date);
    List<CreneauReservation> findByIsDisponibleTrue();
    List<CreneauReservation> findByMedecinIdAndIsDisponibleTrue(String medecinId);
    List<CreneauReservation> findByMedecinIdAndDateAndIsDisponibleTrue(String medecinId, LocalDate date);
}
