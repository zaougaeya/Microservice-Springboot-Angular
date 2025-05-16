package com.example.userservice.controller;

import com.example.userservice.model.*;
import com.example.userservice.service.MaterielService;
import com.example.userservice.service.ReservationService;
import com.example.userservice.service.UserService;
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
    private UserService userService;
    @Autowired
    private MaterielService materielService;

    // Nouvelle classe de requête pour la création de réservation
    @Data
    public static class CreateReservationRequest {
        private String userId;
        private String materielId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest request) {
        try {
            User user = userService.getUserById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Materiel materiel = materielService.getMaterielById(request.getMaterielId())
                    .orElseThrow(() -> new RuntimeException("Materiel not found"));

            Reservation reservation = reservationService.createReservation(
                    user,
                    materiel,
                    request.getStartDate(),
                    request.getEndDate());

            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getUserReservations(@PathVariable String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationService.getUserReservations(user);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Reservation cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public List<Reservation> getByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(
                ReservationStatus.valueOf(status.toUpperCase()));
    }
}