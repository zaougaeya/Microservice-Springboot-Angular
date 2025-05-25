package com.example.gestionequipe.controller;
import com.example.userapi.client.UserClient;
import com.example.userapi.dto.UserResponseDTO;


import com.example.gestionequipe.model.CreneauReservation;
import com.example.gestionequipe.model.ReservationConsultation;
import com.example.gestionequipe.repository.CreneauReservationRepository;
import com.example.gestionequipe.service.ReservationConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequestMapping("reservations")
@CrossOrigin(origins = "http://localhost:4200")
@RestController

public class ReservationConsultationController {

    
    private ReservationConsultationService reservationService;
    
    private CreneauReservationRepository creneauReservationRepository;

    // CREATE
    @PostMapping
    public ReservationConsultation createReservation(@RequestBody ReservationConsultation reservation) {
        return reservationService.saveReservation(reservation);
    }

    // READ all
    @GetMapping
    public List<ReservationConsultation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // READ one
    @GetMapping("/{id}")
    public Optional<ReservationConsultation> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ReservationConsultation updateReservation(@PathVariable String id, @RequestBody ReservationConsultation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
    }


    @GetMapping("/disponibilite")
    public boolean verifierDisponibilite(@RequestParam String creneauId) {
        return creneauReservationRepository.findById(creneauId)
                .map(CreneauReservation::isDisponible)
                .orElse(false);
    }
    @GetMapping("/availableSlots/{medecinId}")
    public List<CreneauReservation> getAvailableSlots(
            @PathVariable String medecinId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.getAvailableTimeSlotsForMedecin(medecinId, date);
    }}

// TODO: Inject UserClient in ReservationConsultationController.java via constructor
