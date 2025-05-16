package com.example.gestionmateriel.repository;

import com.example.userservice.model.Reservation;
import com.example.userservice.model.ReservationStatus;
import com.example.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByMaterielId(String equipmentId);
    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByMaterielIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String materielId, LocalDateTime end, LocalDateTime start);

    List<Reservation> findByUserAndStatus(User user, ReservationStatus status);
    int countByMaterielId(String materieltId);
}