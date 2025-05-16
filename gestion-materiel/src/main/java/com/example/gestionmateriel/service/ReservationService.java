package com.example.gestionmateriel.service;

import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.model.Reservation;
import com.example.gestionmateriel.model.ReservationStatus;
import com.example.gestionmateriel.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MaterielService materielService;

    /**
     * Crée une nouvelle réservation si le matériel est disponible.
     */
    public Reservation createReservation(String reservedBy, Materiel materiel,
                                         LocalDateTime startDate, LocalDateTime endDate) {
        if (!isMaterielAvailable(materiel.getId(), startDate, endDate)) {
            throw new RuntimeException("Matériel indisponible pour cette période.");
        }

        Reservation reservation = new Reservation();
        reservation.setReservedBy(reservedBy);
        reservation.setMateriel(materiel);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(ReservationStatus.CONFIRMED);



        return reservationRepository.save(reservation);
    }

    /**
     * Vérifie si un matériel est disponible entre deux dates.
     */
    public boolean isMaterielAvailable(String materielId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository
                .findByMaterielIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(materielId, end, start)
                .stream()
                .noneMatch(r -> r.getStatus() != ReservationStatus.CANCELLED);
    }

    /**
     * Liste des réservations faites par un "utilisateur" (nom ou service libre).
     */
    public List<Reservation> getReservationsByReservedBy(String reservedBy) {
        return reservationRepository.findByReservedBy(reservedBy);
    }

    /**
     * Annule une réservation.
     */
    public void cancelReservation(String reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        });
    }

    /**
     * Liste les réservations par statut (CONFIRMED, CANCELLED...).
     */
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * Liste toutes les réservations.
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


}
