package com.example.gestionproduit.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestionproduit.model.Voiture;
import com.example.gestionproduit.service.VoitureService;

import java.util.List;

@RestController
@RequestMapping("/api/voitures")
public class VoitureController {

    @Autowired
    private VoitureService voitureService;

    // ➡️ Ajouter une nouvelle voiture
    @PostMapping("/ajouter")
    public Voiture ajouterVoiture(@RequestBody Voiture voiture) {
        return voitureService.ajouterVoiture(voiture);
    }

    // ➡️ Récupérer toutes les voitures
    @GetMapping("/all")
    public List<Voiture> getAllVoitures() {
        return voitureService.getAllVoitures();
    }

    // ➡️ Récupérer les voitures disponibles
    @GetMapping("/disponibles")
    public List<Voiture> getVoituresDisponibles() {
        return voitureService.getVoituresDisponibles();
    }
    @PostMapping("/affecter")
public ResponseEntity<String> affecterVoiture(@RequestParam String idVoiture, @RequestParam String idLivreur) {
    try {
        voitureService.affecterVoitureALivreur(idVoiture, idLivreur);
        return ResponseEntity.ok("Voiture affectée avec succès au livreur.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
/* 
@PostMapping("/calculerCo2")
public ResponseEntity<String> calculerConsommationCO2(@RequestParam String idVoiture,
                                                      @RequestParam String adresse) {
    try {
        double co2 = voitureService.calculerConsommationCO2(idVoiture, adresse);
        return ResponseEntity.ok("Consommation CO2 : " + co2 + " kg");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }*/
}










