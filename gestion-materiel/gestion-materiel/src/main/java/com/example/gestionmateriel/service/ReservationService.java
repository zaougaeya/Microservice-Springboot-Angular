package com.example.gestionmateriel.service;

import com.example.userservice.model.*;
import com.example.userservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MaterielService materielService;

    public Reservation createReservation(User user, Materiel materiel,
                                         LocalDateTime startDate, LocalDateTime endDate) {
        if (!isMaterielAvailable(materiel.getId(), startDate, endDate)) {
            throw new RuntimeException("Materiel not available for selected period");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setMateriel(materiel);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(reservation);
    }

    public boolean isMaterielAvailable(String materielId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository
                .findByMaterielIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        materielId, end, start)
                .stream()
                .noneMatch(r -> r.getStatus() != ReservationStatus.CANCELLED);
    }

    public List<Reservation> getUserReservations(User user) {
        return reservationRepository.findByUser(user);
    }

    public void cancelReservation(String reservationId) {
        reservationRepository.findById(reservationId).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        });
    }

    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
}