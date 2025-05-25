package com.example.userservice.repository;

import com.example.userservice.model.CreneauReservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface CreneauReservationRepository extends MongoRepository<CreneauReservation, String> {
    List<CreneauReservation> findByMedecinIdAndDate(String medecinId, LocalDate date);
    List<CreneauReservation> findByIsDisponibleTrue();
    List<CreneauReservation> findByMedecinIdAndIsDisponibleTrue(String medecinId);
    List<CreneauReservation> findByMedecinIdAndDateAndIsDisponibleTrue(String medecinId, LocalDate date);
}
