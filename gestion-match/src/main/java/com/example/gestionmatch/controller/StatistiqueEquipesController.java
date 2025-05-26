package com.example.gestionmatch.controller;

import com.example.gestionmatch.model.StatistiqueEquipes;
import com.example.gestionmatch.service.StatistiqueEquipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistiquesEquipe")
@CrossOrigin(origins = "http://localhost:4200")
public class StatistiqueEquipesController {

    @Autowired
    private StatistiqueEquipesService statistiquesEquipeService;

    // Ajouter ou mettre à jour des statistiques d'équipe
    @PostMapping
    public ResponseEntity<StatistiqueEquipes> createOrUpdateStatistiquesEquipe(@RequestBody StatistiqueEquipes statistiquesEquipe) {
        return ResponseEntity.ok(statistiquesEquipeService.saveStatistiquesEquipe(statistiquesEquipe));
    }
    @GetMapping
    public ResponseEntity<List<StatistiqueEquipes>> getAllStatistiquesEquipes() {
        return ResponseEntity.ok(statistiquesEquipeService.getAllStatistiquesEquipes());
    }
    @GetMapping("/{id}")
    public ResponseEntity<StatistiqueEquipes> getStatistiquesEquipeById(@PathVariable String id) {
        Optional<StatistiqueEquipes> statistiquesEquipe = statistiquesEquipeService.getStatistiquesEquipeById(id);
        return statistiquesEquipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatistiquesEquipe(@PathVariable String id) {
        statistiquesEquipeService.deleteStatistiquesEquipe(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{equipeId}")
    public StatistiqueEquipes ajouterStatistiques(@PathVariable String equipeId, @RequestBody StatistiqueEquipes stats) {
        return statistiquesEquipeService.ajouterStatistiques(equipeId, stats);
    }
    // Endpoint pour récupérer les statistiques dynamiques d'une équipe
    @GetMapping("/calculer/{equipeId}")
    public ResponseEntity<StatistiqueEquipes> getStatistiquesEquipeDynamique(
            @PathVariable String equipeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
try {
    // Convertir les dates (assurez-vous que les dates sont envoyées dans un format ISO 8601)
    //LocalDate start = LocalDate.parse(startDate);
    //LocalDate end = LocalDate.parse(endDate);

    LocalDateTime startDateTime = LocalDateTime.parse(startDate);
    LocalDateTime endDateTime = LocalDateTime.parse(endDate);

    // Convertir LocalDateTime en LocalDate si l'heure n'est pas nécessaire
    LocalDate start = startDateTime.toLocalDate();
    LocalDate end = endDateTime.toLocalDate();


    StatistiqueEquipes stats = statistiquesEquipeService.calculerStatistiquesDynamique(equipeId,  start, end);

    return ResponseEntity.ok(stats);
}catch (DateTimeParseException e) {
    // Si le format de date est invalide, retourner une erreur
    return ResponseEntity.badRequest().body(null);
}
    }

}
