package com.example.gestionmateriel.controller;

import com.example.gestionmateriel.dto.ReservationCreateDTO;
import com.example.gestionmateriel.dto.ReservationResponseDTO;
import com.example.gestionmateriel.model.Reservation;
import com.example.gestionmateriel.model.ReservationStatus;
import com.example.gestionmateriel.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*") // Ajouté si tu veux accepter les requêtes Angular
public class ReservationController {




    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationCreateDTO request) {
        try {
            Reservation reservation = reservationService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservationService.mapToDTO(reservation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by/{reservedBy}")
    public List<ReservationResponseDTO> getReservationsByReservedBy(@PathVariable String reservedBy) {
        return reservationService.getReservationsByReservedBy(reservedBy)
                .stream().map(reservationService::mapToDTO).collect(Collectors.toList());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Réservation annulée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public List<ReservationResponseDTO> getByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(
                        ReservationStatus.valueOf(status.toUpperCase()))
                .stream().map(reservationService::mapToDTO).collect(Collectors.toList());
    }

    @GetMapping("/calendar")
    public List<ReservationResponseDTO> getAllReservations() {
        return reservationService.getAllReservations()
                .stream().map(reservationService::mapToDTO).collect(Collectors.toList());
    }
}
