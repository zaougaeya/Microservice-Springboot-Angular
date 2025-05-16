package com.example.gestionmateriel.controller;

import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.model.Reservation;
import com.example.gestionmateriel.model.ReservationStatus;
import com.example.gestionmateriel.service.MaterielService;
import com.example.gestionmateriel.service.ReservationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MaterielService materielService;

    // DTO pour la création de réservation
    @Data
    public static class CreateReservationRequest {
        private String reservedBy;
        private String materielId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    /**
     * Créer une réservation
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest request) {
        try {
            Materiel materiel = materielService.getMaterielById(request.getMaterielId())
                    .orElseThrow(() -> new RuntimeException("Matériel introuvable"));

            Reservation reservation = reservationService.createReservation(
                    request.getReservedBy(),
                    materiel,
                    request.getStartDate(),
                    request.getEndDate());

            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtenir les réservations faites par "reservedBy"
     */
    @GetMapping("/by/{reservedBy}")
    public List<Reservation> getReservationsByReservedBy(@PathVariable String reservedBy) {
        return reservationService.getReservationsByReservedBy(reservedBy);
    }

    /**
     * Annuler une réservation
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Réservation annulée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Obtenir les réservations par statut
     */
    @GetMapping("/status/{status}")
    public List<Reservation> getByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(
                ReservationStatus.valueOf(status.toUpperCase()));
    }

    /**
     * Obtenir toutes les réservations (optionnel)
     */
    @GetMapping("/calendar")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

}
