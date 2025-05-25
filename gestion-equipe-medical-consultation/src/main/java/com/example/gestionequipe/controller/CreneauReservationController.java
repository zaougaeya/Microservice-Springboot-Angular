package com.example.userservice.controller;


import com.example.userservice.model.CreneauReservation;
import com.example.userservice.service.CreneauReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("creneaux")
public class CreneauReservationController {
    @Autowired
    private CreneauReservationService service;

    @PostMapping
    public CreneauReservation create(@RequestBody CreneauReservation creneau) {
        return service.createCreneau(creneau);
    }

    @GetMapping
    public List<CreneauReservation> getAll() {
        return service.getAll();
    }

    @GetMapping("/available")
    public List<CreneauReservation> getAvailable() {
        return service.getAvailableCreneaux();
    }

    @GetMapping("/medecin/{medecinId}")
    public List<CreneauReservation> getByMedecinAndDate(
            @PathVariable String medecinId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getByMedecinAndDate(medecinId, date);
    }

    @PutMapping("/{id}/mark-unavailable")
    public void markUnavailable(@PathVariable String id) {
        service.markAsUnavailable(id);
    }

    /*@GetMapping("/medecin/{medecinId}/available")
    public List<CreneauReservation> getAvailableCreneauxForMedecinByDate(
            @PathVariable String medecinId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getAvailableCreneauxForMedecinByDate(medecinId, date);
    }*/


}

