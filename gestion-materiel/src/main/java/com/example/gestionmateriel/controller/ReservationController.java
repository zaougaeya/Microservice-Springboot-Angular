package com.example.gestionmateriel.controller;

import com.example.gestionmateriel.dto.ReservationCreateDTO;
import com.example.gestionmateriel.dto.ReservationResponseDTO;
import com.example.gestionmateriel.model.Materiel;
import com.example.gestionmateriel.model.Reservation;
import com.example.gestionmateriel.model.ReservationStatus;
import com.example.gestionmateriel.service.MaterielService;
import com.example.gestionmateriel.service.ReservationService;
import com.example.userapi.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController{
private ReservationService reservationService;
private final UserClient userClient;
private final MaterielService materielService;

@Autowired
public  ReservationController(ReservationService reservationService, UserClient userClient, MaterielService materielService) {
    this.reservationService =reservationService;
    this.userClient =userClient;
    this.materielService = materielService;
}



    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationCreateDTO request)
    {
        System.out.println("▶️ Reçu payload : " + request);
        try {
            Reservation reservation = reservationService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservationService.mapToDTO(reservation));
        } catch (Exception e) {
            e.printStackTrace(); // 👈 utile pour comprendre pourquoi 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by/{reservedBy}")
    public List<ReservationResponseDTO> getReservationsByReservedBy(@PathVariable String reservedBy) {
        return reservationService.getReservationsByReservedBy(reservedBy)
                .stream().map(reservationService::mapToDTO).collect(Collectors.toList());
    }


    @GetMapping("/materiel")
    public List<Materiel> getAllMateriel() {
        return materielService.getAllMateriel();
    }
    @GetMapping("/materiel/{materielId}")
    public List<Reservation> getByMateriel(@PathVariable String materielId) {
        System.out.println("🔍 Fetching reservations for materielId: " + materielId);
        return reservationService.findByMaterielId(materielId);
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
    @GetMapping("/user")
    public List<ReservationResponseDTO> getUserReservations() {
        // ⚠️ temporairement retourne tout
        return reservationService.getAllReservations()
                .stream().map(reservationService::mapToDTO).collect(Collectors.toList());
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPaiement(@PathVariable String id) {
        try {
            reservationService.confirmReservationAsPaid(id);
            Reservation updated = reservationService.getById(id);
            ReservationResponseDTO dto = reservationService.mapToDTO(updated);
            return ResponseEntity.ok(dto); // ✅ retourne un vrai objet JSON
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
